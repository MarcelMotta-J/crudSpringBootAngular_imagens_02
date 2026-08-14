#!/bin/bash

API="http://localhost:8081"

# Depois torne executável:
# chmod +x api.sh

# Carregue as funções com:
# source ./api.sh
# ou
# . ./api.sh

# executar
# ./api.sh
# =========================================================
# JWT
# =========================================================

require_token() {
    if [ -z "$TOKEN" ]; then
        echo "TOKEN is empty. Run login first."
        return 1
    fi
}

login() {
    local email="${1:-admin@test.com}"

    read -s -p "Password for $email: " password
    echo

    TOKEN=$(
        curl -sS -X POST "$API/api/auth/login" \
            -H "Content-Type: application/json" \
            -d "{
                \"email\":\"$email\",
                \"password\":\"$password\"
            }" |
            jq -r '.token'
    )

    if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
        echo "Login failed"
        unset TOKEN
        return 1
    fi

    export TOKEN

    echo "Login successful"
    echo "JWT loaded into TOKEN"
}

logout() {
    unset TOKEN
    echo "TOKEN removed"
}

show_token() {
    require_token || return 1

    echo "${TOKEN:0:25}..."
}

# =========================================================
# USERS
# =========================================================

users() {
    require_token || return 1

    curl -sS "$API/api/users" \
        -H "Authorization: Bearer $TOKEN" |
        jq
}

user() {
    require_token || return 1

    if [ -z "$1" ]; then
        echo "Usage: user <id>"
        return 1
    fi

    curl -sS "$API/api/users/$1" \
        -H "Authorization: Bearer $TOKEN" |
        jq
}

delete_user() {
    require_token || return 1

    if [ -z "$1" ]; then
        echo "Usage: delete_user <id>"
        return 1
    fi

    curl -i -X DELETE "$API/api/users/$1" \
        -H "Authorization: Bearer $TOKEN"

    echo
}

create_user() {
    require_token || return 1

    local firstname="$1"
    local lastname="$2"
    local email="$3"
    local password="$4"
    local image="$5"

    if [ -z "$firstname" ] || \
       [ -z "$lastname" ] || \
       [ -z "$email" ] || \
       [ -z "$password" ]; then

        echo "Usage:"
        echo "create_user <firstname> <lastname> <email> <password> [image]"
        return 1
    fi

    if [ -n "$image" ]; then
        curl -sS -X POST "$API/api/users" \
            -H "Authorization: Bearer $TOKEN" \
            -F "first_name=$firstname" \
            -F "last_name=$lastname" \
            -F "email=$email" \
            -F "password=$password" \
            -F "image=@$image" |
            jq
    else
        curl -sS -X POST "$API/api/users" \
            -H "Authorization: Bearer $TOKEN" \
            -F "first_name=$firstname" \
            -F "last_name=$lastname" \
            -F "email=$email" \
            -F "password=$password" |
            jq
    fi
}

update_user() {
    require_token || return 1

    local id="$1"
    local firstname="$2"
    local lastname="$3"
    local email="$4"
    local password="$5"
    local image="$6"

    if [ -z "$id" ] || \
       [ -z "$firstname" ] || \
       [ -z "$lastname" ] || \
       [ -z "$email" ] || \
       [ -z "$password" ]; then

        echo "Usage:"
        echo "update_user <id> <firstname> <lastname> <email> <password> [image]"
        return 1
    fi

    if [ -n "$image" ]; then
        curl -sS -X PUT "$API/api/users/$id" \
            -H "Authorization: Bearer $TOKEN" \
            -F "first_name=$firstname" \
            -F "last_name=$lastname" \
            -F "email=$email" \
            -F "password=$password" \
            -F "image=@$image" |
            jq
    else
        curl -sS -X PUT "$API/api/users/$id" \
            -H "Authorization: Bearer $TOKEN" \
            -F "first_name=$firstname" \
            -F "last_name=$lastname" \
            -F "email=$email" \
            -F "password=$password" |
            jq
    fi
}

search_users() {
    require_token || return 1

    if [ -z "$1" ]; then
        echo "Usage: search_users <query>"
        return 1
    fi

    curl -sS "$API/api/users/search?query=$1" \
        -H "Authorization: Bearer $TOKEN" |
        jq
}
