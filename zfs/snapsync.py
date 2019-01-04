#!/usr/bin/python3
import sys
import subprocess

src = sys.argv[1]
dest = sys.argv[2]

def get_latest_snapshot(dataset):
	result = subprocess.run(['zfs','list','-H','-o','name','-r','-t','snapshot',dataset], stdout=subprocess.PIPE)
	snapshots = result.stdout.split(b'\n')
	latest_snapshot = snapshots[-2]
	return latest_snapshot.decode("utf-8")

latest_src = get_latest_snapshot(src)
latest_dest = get_latest_snapshot(dest)
from_snap = src + '@' + latest_dest.split('@')[1]
to_snap = src + '@' + latest_src.split('@')[1]
print("From: " + from_snap)
print("To: " + to_snap)

shellcmd = 'zfs send -I ' + from_snap + ' ' + to_snap + ' | zfs recv ' + dest
print('Running: ' + shellcmd)
subprocess.run(shellcmd, shell=True)
