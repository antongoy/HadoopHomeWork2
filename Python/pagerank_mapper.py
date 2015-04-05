#!/usr/bin/env python
from __future__ import print_function

import sys

__author__ = 'anton-goy'

for line in sys.stdin:
    line = line.strip('\n')

    _, page_rank, out_nodes = line.split('\t')

    if out_nodes == '':
        continue
    else:
        out_nodes = [int(node) for node in out_nodes.split(',')]

    page_rank = float(page_rank)
    node_degree = len(out_nodes)

    print(line)

    for node in out_nodes:
        print(node, page_rank / node_degree, '', sep='\t')
