using System.Runtime.Serialization;

namespace AgentService.Protocol
{
    [DataContract]
    public class SetPasswordRequest
    {
        [DataMember(Name = "requestType")]
        public string RequestType { get; set; }
        [DataMember(Name = "userName")]
        public string UserName { get; set; }
        [DataMember(Name = "password")]
        public string Password { get; set; }
    }
}
