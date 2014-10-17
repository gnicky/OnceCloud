using System.Runtime.Serialization;

namespace AgentService.Message
{
    [DataContract]
    public class Response
    {
        [DataMember(Name = "responseType")]
        public string ResponseType { get; set; }
    }
}
