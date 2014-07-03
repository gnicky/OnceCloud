#!/bin/bash
FILE_NAME=/tmp/netsh.tar.gz
echo "Net shell installer"
sed -n -e '1,/^exit 0$/!p' $0 > $FILE_NAME 2>/dev/null
rm -rf /usr/local/netsh
mkdir -p /usr/local/netsh
tar -zxf $FILE_NAME -C /usr/local/netsh
rm -rf $FILE_NAME
echo "Installed to /usr/local/netsh. Enter \"netsh\" for more information."
echo "Document directory: /usr/local/netsh/doc"
chmod +x /usr/local/netsh/netsh
ln -sf /usr/local/netsh/netsh /bin/netsh
echo "Done."
exit 0
