#!/bin/bash

set -e

exec "$@" --spring.profiles.active=${active}