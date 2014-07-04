#!/bin/bash
FILE_NAME=/tmp/netsh.tar.gz

sed -n -e '1,/^exit 0$/!p' $0 > $FILE_NAME 2>/dev/null

rm -rf /usr/local/netsh
mkdir -p /usr/local/netsh

tar -zxf $FILE_NAME -C /usr/local/netsh
rm -rf $FILE_NAME

chmod +x /usr/local/netsh/netsh
ln -sf /usr/local/netsh/netsh /bin/netsh

chmod +x /usr/local/netsh/netd
ln -sf /usr/local/netsh/netd /bin/netd
exit 0
