#!/bin/sh

script_path=$(dirname $(realpath "$0"))

kubectl apply -f $script_path/../namespace.yaml
kubectl apply -f $script_path/../banana-crd.yaml
kubectl apply -f $script_path/../rbac.yaml
kubectl apply -f $script_path/../deployment.yaml