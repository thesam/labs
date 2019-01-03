#!/usr/bin/python3
import sys
import subprocess

src = sys.argv[1]
dest = sys.argv[2]

def get_latest_snapshot(dataset):
	result = subprocess.run(['zfs','list','-H','-o','name','-r','-t','snapshot',dataset], stdout=subprocess.PIPE)
	snapshots = result.stdout.split(b'\n')
	latest_snapshot = snapshots[-2]
	return latest_snapshot

print(get_latest_snapshot(src))
print(get_latest_snapshot(dest))

