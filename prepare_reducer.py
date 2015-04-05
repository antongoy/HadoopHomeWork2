#!/usr/bin/env python
from __future__ import print_function

import sys

__author__ = 'anton-goy'

current_key = None
cited_values = []
default_page_rank = 0.15

for line in sys.stdin:
    key, value = line.strip('\n').split('\t')

    if value:
        value = int(value)

    if not current_key:
        current_key = key

    if key != current_key:
        cited_values = ','.join(str(c) for c in cited_values)
        print(current_key, default_page_rank, cited_values, sep='\t')

        current_key = key
        cited_values = []

    if value:
        cited_values.append(value)

cited_values = ','.join(str(c) for c in cited_values)
print(current_key, default_page_rank, cited_values, sep='\t')

