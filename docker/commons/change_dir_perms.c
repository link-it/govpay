#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>

// Funzione per modificare permessi di una directory
void modify_directory_permissions(const char *dir) {
    struct stat dir_stat;
    uid_t user_id = getuid();
    gid_t group_id = getgid();

    // Ottieni le informazioni sulla directory
    if (stat(dir, &dir_stat) == -1) {
        perror("stat");
        return;
    }

    uid_t dir_user_id = dir_stat.st_uid;
    gid_t dir_group_id = dir_stat.st_gid;

    // Caso 1: L'UID e il GID coincidono con quelli dell'utente corrente
    if (user_id == dir_user_id && group_id == dir_group_id) {
        // Non fare nulla
        return;
    }
    // Caso 2: UID e GID diversi
    else if (user_id != dir_user_id && group_id != dir_group_id) {
        // Modifica la proprietà e i permessi
        if (chown(dir, dir_user_id, group_id) == -1) {
            perror("chown");
        }
        if (chmod(dir, S_IRWXU | S_IRWXG) == -1) {
            perror("chmod");
        }
    }
    // Caso 3: UID diverso, ma stesso GID
    else if (user_id != dir_user_id && group_id == dir_group_id) {
        // Modifica solo i permessi
        if (chmod(dir, S_IRWXU | S_IRWXG) == -1) {
            perror("chmod");
        }
    }
    // Caso 4: UID uguale, ma GID diverso
    else if (user_id == dir_user_id && group_id != dir_group_id) {
        // Non fare nulla
        return;
    }
}

int main(int argc, char *argv[]) {
    // Definisci le directory da processare come argomenti
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <directory1> <directory2> ...\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    // Loop attraverso ogni directory passata come argomento
    for (int i = 1; i < argc; i++) {
        modify_directory_permissions(argv[i]);
    }

    return 0;
}
