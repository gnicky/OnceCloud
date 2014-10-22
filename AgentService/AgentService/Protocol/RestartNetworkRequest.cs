using System.Runtime.Serialization;

namespace AgentService.Protocol
{
    [DataContract]
    public class RestartNetworkRequest
    {
        [DataMember(Name = "requestType")]
        public string RequestType { get; set; }
    }
}
