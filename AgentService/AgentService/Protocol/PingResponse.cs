﻿using System.Runtime.Serialization;

namespace AgentService.Protocol
{
    [DataContract]
    public class PingResponse
    {
        [DataMember(Name = "requestType")]
        public string ResponseType { get; set; }
    }
}
