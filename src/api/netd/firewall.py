import httplib
import json
from netd import endpoint

def get_configuration(location):
    connection=httplib.HTTPConnection(location)
    connection.request("GET",endpoint.Firewall.get_configuration)
    response=connection.getresponse()
    content=response.read()
    connection.close()
    return content

def add_rule(location,protocol,internal_ip_range,external_ip_range,port):
    request_headers={
        "x-bws-protocol":protocol,
        "x-bws-internal-ip-range":internal_ip_range,
        "x-bws-port":port
    }

    if(external_ip_range!=None):
        request_headers["x-bws-external-ip-range"]=external_ip_range

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.Firewall.add_rule,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def remove_rule(location,protocol,internal_ip_range,external_ip_range,port):
    request_headers={
        "x-bws-protocol":protocol,
        "x-bws-internal-ip-range":internal_ip_range,
        "x-bws-port":port
    }

    if(external_ip_range!=None):
        request_headers["x-bws-external-ip-range"]=external_ip_range

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.Firewall.remove_rule,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def set_default_rules(location):
    connection=httplib.HTTPConnection(location)
    connection.request("PUT",endpoint.Firewall.set_rules,"")
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def set_rules(location,ip,rules):
    request_content={
        "IP":ip,
        "rules":rules
    }

    connection=httplib.HTTPConnection(location)
    connection.request("PUT",endpoint.Firewall.set_rules,json.dumps(request_content))
    response=connection.getresponse()
    connection.close()
    return (response.status==200)
    
