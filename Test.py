import os

command = "git log --pretty=format:\"%H\" -n " + str(50) + " > " + "commit_log.pj"
if os.system(command) != 0:
    print(-1)

commit_log_file = open("commit_log.pj", "r")

all_log = commit_log_file.read().strip().split("\n")
commit_log_file.close()

print(all_log)