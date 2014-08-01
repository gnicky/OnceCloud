import httplib
import json
from netd import endpoint

def add_port_forwarding(location,protocol,internal_ip_address,internal_port,external_ip_address,external_port):
    request_headers={
        "x-bws-protocol":protocol,
        "x-bws-internal-ip-address":internal_ip_address,
        "x-bws-internal-port":internal_port,
        "x-bws-external-ip-address":external_ip_address,
        "x-bws-external-port":external_port
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.NAT.add_port_forwarding,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def remove_port_forwarding(location,protocol,internal_ip_address,internal_port,external_ip_address,external_port):
    request_headers={
        "x-bws-protocol":protocol,
        "x-bws-internal-ip-address":internal_ip_address,
        "x-bws-internal-port":internal_port,
        "x-bws-external-ip-address":external_ip_address,
        "x-bws-external-port":external_port
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.NAT.remove_port_forwarding,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

