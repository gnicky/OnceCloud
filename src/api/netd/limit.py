import httplib
import json
from netd import endpoint

def get_configuration(location,interface):
    request_headers={
        "x-bws-interface":interface
    }

    connection=httplib.HTTPConnection(location)
    connection.request("GET",endpoint.Limit.get_configuration,"",request_headers)
    response=connection.getresponse()
    content=response.read()
    connection.close()
    return content

def add_class(location,interface,class_id,speed):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-class-id":class_id,
        "x-bws-speed":speed
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.Limit.add_class,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def add_filter(location,interface,flow_id):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-flow-id":flow_id
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.Limit.add_filter,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def add_ip(location,interface,gateway,ip,flow_id):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-gateway":gateway,
        "x-bws-ip-address":ip,
        "x-bws-flow-id":flow_id
    }

    connection=httplib.HTTPConnection(location)
    connection.request("POST",endpoint.Limit.add_ip,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def set_default_rules(location,interface):
    request_headers={
        "x-bws-interface":interface
    }

    connection=httplib.HTTPConnection(location)
    connection.request("PUT",endpoint.Limit.set_default_rules,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def remove_class(location,interface,class_id):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-class-id":class_id
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.Limit.remove_class,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def remove_filter(location,interface,flow_id):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-flow-id":flow_id
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.Limit.remove_filter,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

def remove_ip(location,interface,gateway,ip):
    request_headers={
        "x-bws-interface":interface,
        "x-bws-gateway":gateway,
        "x-bws-ip-address":ip
    }

    connection=httplib.HTTPConnection(location)
    connection.request("DELETE",endpoint.Limit.remove_ip,"",request_headers)
    response=connection.getresponse()
    connection.close()
    return (response.status==200)

