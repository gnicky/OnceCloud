import httplib
import json
from netd import endpoint

def configure(location,configuration):
    print(json.dumps(configuration))
    connection=httplib.HTTPConnection(location)
    connection.request("PUT",endpoint.LoadBalancer.configure,json.dumps(configuration))
    response=connection.getresponse()
    print(response.status)
    connection.close()
    return (response.status==200)
