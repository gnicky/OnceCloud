using System.Runtime.Serialization;

namespace AgentService.Message
{
    [DataContract]
    public class RestartNetworkRequest
    {
        [DataMember(Name = "requestType")]
        public string RequestType { get; set; }
    }
}
