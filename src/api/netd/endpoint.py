class DHCP:
    add_subnet="/DHCP"
    add_hosts="/DHCP?host"
    assign_ip_address="/DHCP?assign"
    clean_up_configuration="/DHCP"
    get_configuration="/DHCP"
    remove_subnet="/DHCP"
    remove_hosts="/DHCP?host"

class NAT:
    add_binding="/NAT"
    add_port_forwarding="/NAT?port"
    clean_up_configuration="/NAT"
    get_configuration="/NAT"
    remove_binding="/NAT"
    remove_port_forwarding="/NAT?port"

class Firewall:
    add_rule="/Firewall"
    remove_rule="/Firewall"
    get_rules="/Firewall"
    set_rules="/Firewall"
    get_ping_rules="/Firewall?ping"
    add_ping_rule="/Firewall?ping"
    remove_ping_rule="/Firewall?ping"

class Limit:
    get_configuration="/Limit"
    add_class="/Limit?class"
    add_filter="/Limit?filter"
    add_ip="/Limit?ip"
    set_default_rules="/Limit"

