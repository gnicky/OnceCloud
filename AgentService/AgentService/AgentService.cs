﻿using AgentService.Message;
using AgentService.Helper;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.IO.Ports;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.IO;

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
                    }
                }
            }
            catch
            {

            }
        }
    }
}
