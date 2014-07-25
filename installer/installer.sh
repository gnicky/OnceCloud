#!/bin/bash
FILE_NAME=/tmp/netd.tar.gz

sed -n -e '1,/^exit 0$/!p' $0 > $FILE_NAME 2>/dev/null

rm -rf /usr/local/netd/plugins/*
rm -rf /usr/local/netd/netd

mkdir -p /usr/local/netd
mkdir -p /usr/local/netd/plugins
mkdir -p /usr/local/netd/log

tar -zxf $FILE_NAME -C /usr/local/netd
rm -rf $FILE_NAME

chmod +x /usr/local/netd/netd
ln -sf /usr/local/netd/netd /bin/netd

exit 0
