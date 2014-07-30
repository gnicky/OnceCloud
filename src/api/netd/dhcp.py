import httplib
import json
from netd import endpoint

def get_configuration(location):
    connection=httplib.HTTPConnection(location)
    connection.request("GET",endpoint.DHCP.get_configuration)
    response=connection.getresponse()
    content=response.read()
    connection.close()
    return content

def add_subnet(location,subnet,netmask,router,dns,rangeStart,rangeEnd,defaultLease,maxLease):
    request_content={
        "subnet":subnet,
        "netmask":netmask,
        "router":router,
        "dns":dns,
        "rangeStart":rangeStart,
        "rangeEnd":rangeEnd,
        "defaultLease":defaultLease,
        "maxLease":maxLease
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.DHCP.add_subnet,json.dumps(request_content))
    response=connection.getresponse()
    result=str(response.status)+" "+response.reason
    connection.close()
    return result

def assign_ip_address(location,mac,subnet):
    request_headers={
        "x-bws-hardware-address":mac,
        "x-bws-subnet-address":subnet
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.DHCP.assign_ip_address,"",request_headers)
    response=connection.getresponse()
    result=str(response.status)+" "+response.reason
    if(response.status==200):
        assigned_ip=response.getheader("x-bws-assigned-ip-address")
    else:
        assigned_ip=None
    connection.close()
    return assigned_ip

def add_hosts(location,hosts):
    request_content={
        "hosts":hosts
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.DHCP.add_hosts,json.dumps(request_content))
    response=connection.getresponse()
    result=str(response.status)+" "+response.reason
    connection.close()
    return result

def clean_up_configuration(location):
    connection=httplib.HTTPConnection(location)
    connection.request("PUT",endpoint.DHCP.clean_up_configuration)
    response=connection.getresponse()
    result=str(response.status)+" "+response.reason
    connection.close()
    return result

def remove_subnet(location,subnet,netmask):
    request_content={
        "subnet":subnet,
        "netmask":netmask
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.DHCP.remove_subnet,json.dumps(request_content))
    response=connection.getresponse()
    result=str(response.status)+" "+response.reason
    connection.close()
    return result

def remove_hosts(location,hosts):
    request_content={
        "hosts":hosts
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.DHCP.remove_hosts,json.dumps(request_content))
    response=connection.getresponse()
    result=str(response.status)+" "+response.reason
    connection.close()
    return result
