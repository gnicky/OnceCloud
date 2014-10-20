using System.Runtime.Serialization;

namespace AgentService.Protocol
{
    [DataContract]
    public class PingRequest
    {
        [DataMember(Name = "requestType")]
        public string RequestType { get; set; }
    }
}
