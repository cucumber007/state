alias rs := run-server
alias rc := run-client

run-server:
    #!/usr/bin/env sh
    set -euo pipefail

    cd server && just run

run-client:
    #!/usr/bin/env sh
    set -euo pipefail

    cd web && just run
