import httplib
import json
from netd import endpoint

def add_binding(location,internal_ip_address,external_ip_address,external_interface):
    request_headers={
        "x-bws-internal-ip-address":internal_ip_address,
        "x-bws-external-ip-address":external_ip_address,
        "x-bws-external-interface":external_interface
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.NAT.add_binding,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

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

def clean_up_configuration(location):
    connection=httplib.HTTPConnection(location)
    connection.request("PUT",endpoint.NAT.clean_up_configuration)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def get_configuration(location):
    connection=httplib.HTTPConnection(location)
    connection.request("GET",endpoint.NAT.get_configuration)
    response=connection.getresponse()
    content=response.read()
    connection.close()
    return content

def remove_binding(location,internal_ip_address,external_ip_address,external_interface):
    request_headers={
        "x-bws-internal-ip-address":internal_ip_address,
        "x-bws-external-ip-address":external_ip_address,
        "x-bws-external-interface":external_interface
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.NAT.remove_binding,"",request_headers)
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

