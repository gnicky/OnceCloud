using System.IO;
using System.Runtime.Serialization.Json;
using System.Text;

namespace AgentService.Helper
{
    public static class JsonHelper
    {
        public static string ToString(object jsonObject)
        {
            using (var ms = new MemoryStream())
            {
                new DataContractJsonSerializer(jsonObject.GetType()).WriteObject(ms, jsonObject);
                return Encoding.ASCII.GetString(ms.ToArray());
            }
        }

        public static T ToObject<T>(string jsonString)
        {
            using (var stream = new MemoryStream(Encoding.UTF8.GetBytes(jsonString)))
            {
                return (T)(new DataContractJsonSerializer(typeof(T)).ReadObject(stream));
            }
        }
    }
}
