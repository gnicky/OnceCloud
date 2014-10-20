using System.Runtime.Serialization;

namespace AgentService.Protocol
{
    [DataContract]
    public class Response
    {
        [DataMember(Name = "responseType")]
        public string ResponseType { get; set; }
    }
}
