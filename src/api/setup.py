import os
from setuptools import setup, find_packages
import netd

setup(
    name="netd",
    version=netd.__version__,
    description="Python API for BeyondCloud Net Daemon",
    author="Zhen Tang",
    packages=['netd'],
)
