using System.Runtime.Serialization;

namespace AgentService.Message
{
    [DataContract]
    public class Request
    {
        [DataMember(Name = "requestType")]
        public string RequestType { get; set; }
    }
}
