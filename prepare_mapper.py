#!/usr/bin/env python
from __future__ import print_function

import sys

__author__ = 'anton-goy'

for line in sys.stdin:

    if line.startswith('"'):
        continue

    line = line.strip()
    key, value = line.split(',')

    print(key, value, sep='\t')
    print(value, '', sep='\t')

