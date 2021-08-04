import sys
import os
from PyQt5.QtWidgets import (QWidget, QLabel, QLineEdit, QComboBox, QPushButton,
                             QGridLayout, QApplication, QDesktopWidget, QMessageBox)

PARAMETER_CHECKED = 0
BRANCH_EMPTY = 1
BRANCH_NOT_EXIST = 2
BRANCH_DUPLICATE = 3
MERGE_FIRST = 4

MAX_COMMIT_NUM = 50

LOG_PATH = "commit_log.pj"
BASE_COMMIT = "base_commit_id.pj"


class CherryPickHelper(QWidget):

    def __init__(self):
        super().__init__()

        # init components
        self.o_branch_edit = QLineEdit()
        self.t_combo = QComboBox(self)
        self.n_t_combo = QComboBox(self)
        self.confirm = QPushButton('Confirm', self)

        self.initUI()

    def initUI(self):
        # layout -> grid
        grid = QGridLayout()
        grid.setSpacing(10)

        # original branch
        o_title = QLabel('Source Branch')
        grid.addWidget(o_title, 1, 0)
        grid.addWidget(self.o_branch_edit, 1, 1)

        # target branch
        t_title = QLabel('Target Branch')
        self.t_combo.addItems(get_legal_branch())
        grid.addWidget(t_title, 2, 0)
        grid.addWidget(self.t_combo, 2, 1)

        # new target branch
        n_t_title = QLabel('New Target Branch')
        self.n_t_combo.addItems(get_legal_branch())
        grid.addWidget(n_t_title, 3, 0)
        grid.addWidget(self.n_t_combo, 3, 1)

        # button
        grid.addWidget(self.confirm, 4, 0, 1, 2)
        self.confirm.clicked.connect(self.on_click_confirm)

        # center on screen
        self.setLayout(grid)
        self.setGeometry(300, 300, 500, 300)
        self.center()
        self.setWindowTitle('CherryPickHelper')
        self.show()

    def center(self):
        qr = self.frameGeometry()
        cp = QDesktopWidget().availableGeometry().center()
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def on_click_confirm(self):
        print("on click confirm")

        o_branch = self.o_branch_edit.text()
        t_branch = self.t_combo.currentText()
        n_t_branch = self.n_t_combo.currentText()

        check_res = check_branches(o_branch, t_branch, n_t_branch)
        if check_res != PARAMETER_CHECKED:
            QMessageBox.warning(self, "Error", construct_error_msg(check_res), QMessageBox.Yes)
            return

        pick_res = int(cherry_pick(o_branch, t_branch, n_t_branch))
        if pick_res != 0:
            QMessageBox.warning(self, "Error", construct_error_msg(pick_res), QMessageBox.Yes)
        else:
            QMessageBox.information(
                self, "Success",
                "\nCherry-pick Success! Your new source branch is\n" + o_branch + "_pick",
                QMessageBox.Yes)

        # TODO: Bits POST?


def cherry_pick(old, target, new):
    if os.path.exists(LOG_PATH):
        return cherry_pick_with_log(old, target, new)

    command = "git checkout " + old
    if os.system(command) != 0:
        return -1
    command = "git pull origin " + old
    if os.system(command) != 0:
        return -1

    command = "git log --pretty=format:\"%H\" -n " + str(MAX_COMMIT_NUM) + " > " + LOG_PATH
    if os.system(command) != 0:
        return -1

    command = "git checkout " + new
    if os.system(command) != 0:
        return -1
    command = "git pull origin " + new
    if os.system(command) != 0:
        return -1

    command = "git checkout -b " + old + "_pick"
    if os.system(command) != 0:
        return -1

    command = "git merge-base " + old + " " + target + " > " + BASE_COMMIT
    if os.system(command) != 0:
        return -1

    base_commit_file = open(BASE_COMMIT, "r")
    base_commit = base_commit_file.read().strip()
    base_commit_file.close()

    all_log = read_logs_from_file()
    i = all_log.index(base_commit)
    if not i >= 0:
        print("Base commit not found!")
        return -1

    commit_cid_list = all_log[:i]
    commit_cid_list.reverse()

    for cid in commit_cid_list:
        command = "git cherry-pick " + cid
        if os.system(command) != 0:
            left_cid_list = commit_cid_list[commit_cid_list.index(cid) + 1:]
            left_cid_list.reverse()
            write_to_log_file(left_cid_list)
            return MERGE_FIRST

    command = "git push origin " + old + "_pick"
    if os.system(command) != 0:
        return -1

    remove_cache_files()
    return 0


def remove_cache_files():
    os.remove(BASE_COMMIT)
    os.remove(LOG_PATH)


def write_to_log_file(cid_list):
    file = open(LOG_PATH, "w")
    for cid in cid_list:
        file.write(cid + "\n")
    file.close()


def read_logs_from_file():
    log_file = open(LOG_PATH, "r")
    logs = log_file.read().strip().split("\n")
    log_file.close()
    return logs


def cherry_pick_with_log(old, target, new):
    all_log = read_logs_from_file()
    all_log.reverse()

    for cid in all_log:
        command = "git cherry-pick " + cid
        if os.system(command) != 0:
            left_cid_list = all_log[all_log.index(cid) + 1:]
            left_cid_list.reverse()
            write_to_log_file(left_cid_list)
            return MERGE_FIRST

    remove_cache_files()
    return 0


def get_legal_branch():
    return "master", "develop", "carplay/develop", "carplay/9th_launch_9.9.9x", "carplay/8th_launch_9.9.8x"


def check_branches(o_b, t_b, n_b):
    print("Old Branch: " + o_b)
    print("Target Branch: " + t_b)
    print("New Target Branch: " + n_b)

    if str(o_b) == "" or str(o_b) is None:
        return BRANCH_EMPTY

    command = "git fetch origin " + o_b
    if os.system(command) != 0:
        return BRANCH_NOT_EXIST

    if t_b == n_b:
        return BRANCH_DUPLICATE

    return PARAMETER_CHECKED


def construct_error_msg(error_type):
    if error_type == BRANCH_EMPTY:
        return "\nYour source branch is empty, Idiot!"
    elif error_type == BRANCH_NOT_EXIST:
        return "\nYour source branch does not exist, Idiot!"
    elif error_type == BRANCH_DUPLICATE:
        return "\nThe two target branches chosen by you is duplicated, Idiot!"
    elif error_type == MERGE_FIRST:
        return "\nCherry-pick needs merge first! Please merge in AS first!"
    else:
        return "\nSome errors happened! Please read the console output!"


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = CherryPickHelper()
    sys.exit(app.exec_())
