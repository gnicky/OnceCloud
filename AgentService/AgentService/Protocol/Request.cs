using System.Runtime.Serialization;

namespace AgentService.Protocol
{
    [DataContract]
    public class Request
    {
        [DataMember(Name = "requestType")]
        public string RequestType { get; set; }
    }
}
