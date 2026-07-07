#!/bin/bash
echo "=== Iniciando Bateria de Testes ==="
for file in tests/test_*.txt; do
    echo "-----------------------------------"
    echo "Executando: $file"
    cp $file entrada.txt
    mvn exec:java -Dexec.mainClass=br.edu.compiladorjava.App -q <<< "5" 2>&1 | grep -E "SUCESSO|ERRO"
done
echo "=== Fim dos Testes ==="
