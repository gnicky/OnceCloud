using AgentService.Helper;
using AgentService.Protocol;
using System;
using System.Diagnostics;
using System.DirectoryServices;
using System.IO;
using System.IO.Ports;
using System.ServiceProcess;
using System.Text;
using System.Threading;

namespace AgentService
{
    public partial class AgentService : ServiceBase
    {
        private bool Running { get; set; }
        private Thread WorkingThread { get; set; }
        private const string LogFile = @"C:\AgentService.log";

        public AgentService()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            this.Running = true;
            this.WorkingThread = new Thread(new ThreadStart(this.DoWork));
            this.WorkingThread.Start();
            File.AppendAllText(AgentService.LogFile
                , string.Format("[{0}] Service started." + Environment.NewLine, DateTime.Now));
        }

        protected override void OnStop()
        {
            this.Running = false;
            this.WorkingThread.Abort();
            File.AppendAllText(AgentService.LogFile
                , string.Format("[{0}] Service stopped." + Environment.NewLine, DateTime.Now));
        }

        private void DoRead(SerialPort serialPort, byte[] buffer, int count)
        {
            int readBytes = 0;
            int remainingBytes = count;
            while (remainingBytes > 0 && this.Running)
            {
                int alreadyRead = serialPort.Read(buffer, readBytes, remainingBytes);
                readBytes += alreadyRead;
                remainingBytes -= alreadyRead;
            }
        }

        private void DoWork()
        {
            try
            {
                using (var serialPort = new SerialPort("COM1", 115200, Parity.None, 8, StopBits.One))
                {
                    serialPort.Open();
                    int magicNumber = 12345678;
                    serialPort.Write(BitConverter.GetBytes(magicNumber), 0, 4);
                    while (this.Running)
                    {
                        byte[] requestLengthBuffer = new byte[4];
                        DoRead(serialPort, requestLengthBuffer, 4);
                        if (!this.Running)
                        {
                            break;
                        }
                        int requestLength = BitConverter.ToInt32(requestLengthBuffer, 0);
                        byte[] contentBuffer = new byte[requestLength];
                        DoRead(serialPort, contentBuffer, requestLength);
                        if (!this.Running)
                        {
                            break;
                        }
                        string content = Encoding.ASCII.GetString(contentBuffer);
                        File.AppendAllText(AgentService.LogFile
                            , string.Format("[{0}] Get Request: {1}" + Environment.NewLine, DateTime.Now, content));
                        var requestType = JsonHelper.ToObject<Request>(content).RequestType;
                        if (requestType == "Agent.RestartNetwork")
                        {
                            var restartNetworkRequest = JsonHelper.ToObject<RestartNetworkRequest>(content);
                            File.AppendAllText(AgentService.LogFile
                                , string.Format("[{0}] Running ipconfig /release" + Environment.NewLine, DateTime.Now));
                            Process.Start(
                                new ProcessStartInfo()
                                {
                                    FileName = "ipconfig.exe",
                                    Arguments = "/release",
                                    CreateNoWindow = true,
                                    UseShellExecute = false
                                }).WaitForExit();

                            File.AppendAllText(AgentService.LogFile
                                , string.Format("[{0}] Running ipconfig /renew" + Environment.NewLine, DateTime.Now));
                            Process.Start(
                                new ProcessStartInfo()
                                {
                                    FileName = "ipconfig.exe",
                                    Arguments = "/renew",
                                    CreateNoWindow = true,
                                    UseShellExecute = false
                                }).WaitForExit();

                            var restartNetworkResponse = new RestartNetworkResponse()
                            {
                                ResponseType = "Agent.RestartNetwork",
                                Result = true
                            };
                            var responseString = JsonHelper.ToString(restartNetworkResponse);
                            serialPort.Write(BitConverter.GetBytes(responseString.Length), 0, 4);
                            serialPort.Write(responseString);
                            File.AppendAllText(AgentService.LogFile
                                , string.Format("[{0}] Send Response: {1}" + Environment.NewLine, DateTime.Now, responseString));
                        }
                        else if (requestType == "Agent.Ping")
                        {
                            var pingRequest = JsonHelper.ToObject<PingRequest>(content);
                            var pingResponse = new PingResponse()
                            {
                                ResponseType = "Agent.Ping",
                                Result = true
                            };
                            var responseString = JsonHelper.ToString(pingResponse);
                            serialPort.Write(BitConverter.GetBytes(responseString.Length), 0, 4);
                            serialPort.Write(responseString);
                            File.AppendAllText(AgentService.LogFile
                                , string.Format("[{0}] Send Response: {1}" + Environment.NewLine, DateTime.Now, responseString));
                        }
                        else if (requestType == "Agent.SetPassword")
                        {
                            var setPasswordRequest = JsonHelper.ToObject<SetPasswordRequest>(content);
                            bool result = true;

                            try
                            {
                                DirectoryEntry localMachine = new DirectoryEntry("WinNT://" + Environment.MachineName + ",computer");
                                DirectoryEntry user = localMachine.Children.Find(setPasswordRequest.UserName, "User");
                                object[] password = new object[] { setPasswordRequest.Password };
                                object ret = user.Invoke("SetPassword", password);
                                user.CommitChanges();
                                localMachine.Close();
                                user.Close();
                            }
                            catch (Exception ex)
                            {
                                File.AppendAllText(AgentService.LogFile
                                    , string.Format("[{0}] Exception: {1}" + Environment.NewLine, DateTime.Now
                                    , ex.InnerException == null ? ex.Message : ex.InnerException.Message));
                                result = false;
                            }

                            var setPasswordResponse = new SetPasswordResponse()
                            {
                                Result = result,
                                ResponseType = "Agent.SetPassword"
                            };
                            var responseString = JsonHelper.ToString(setPasswordResponse);
                            serialPort.Write(BitConverter.GetBytes(responseString.Length), 0, 4);
                            serialPort.Write(responseString);
                            File.AppendAllText(AgentService.LogFile
                                , string.Format("[{0}] Send Response: {1}" + Environment.NewLine, DateTime.Now, responseString));
                        }
                    }
                }
            }
            catch
            {

            }
        }
    }
}
