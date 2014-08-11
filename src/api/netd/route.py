import httplib
import json
from netd import endpoint

def connect(location,interface,ip,netmask):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-ip-address":ip,
        "x-bws-netmask":netmask
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.Route.connect,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def disconnect(location,interface):
    request_headers={
        "x-bws-interface":interface,
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.Route.disconnect,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

