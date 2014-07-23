#!/bin/bash
# Universidad Simon Bolivar.
# Departamento de Computacion y Tecnologia de la Informacion.
# Sistemas de Operacion II (CI-4821).
# Trimestre Abril - Julio 2009.
# Profesora Yudith Cardinale.
# Nombre del archivo: generate_process_file.bash
# Descripcion: Script bash que genera un archivo XML que contiene la informacion
#              de los procesos que seran utilizados en la simulacion del
#              Algoritmo de Scheduling de Linux.
#              Parametros de entrada:
#                 - Numero de procesos.
#                 - Tiempo maximo de llegada de los procesos.
#                 - Duracion maxima de cada proceso.
# Elaborado por: Grupo #2.

if [ "$#" -ne 3 ]; then 
	echo "    Error: generate_process_file.bash <number of processes> <Max arrival time> <Max life time>"
	exit
fi

file_name="process_request_file.xml"

# Si ya existe un archivo previo con el nombre process_file.txt se borra.
if [ -f $file_name ]; then
	echo "    Previos $file_name found."
	echo "    It will be substituted for a new one."
	rm $file_name
fi

num_process=$1
max_arrival_time=$2
max_life_time=$3

echo "<ProcessInfo>" >> $file_name

for ((process_it = 0; process_it<num_process; process_it++)) 
do
	
	echo "    <Process>" >> $file_name
	
	# Tipo de proceso (Round Robin o Real Time)
	# Cota superior para la probabilidad del tipo de proceso.
	ptype_upper_bound=10
	# Limite que diferencia la probabilidad de cada proceso.
	ptype_def_bound=2
	let "process_type = $RANDOM % ($ptype_upper_bound+1)"
	
	#Prioridad del proceso.
	if [ "$process_type" -le $ptype_def_bound ]; then
	    let "process_priority = $RANDOM % 101"
	else
	    let "process_priority = 100 + ($RANDOM % 41)"
	fi
	echo "        <Priority>$process_priority</Priority>" >> $file_name
	
	# Tiempo de llegada del proceso.
	let "arrival_time = $RANDOM % ($max_arrival_time+1)"
	echo "        <ArrivalTime>$arrival_time</ArrivalTime>" >> $file_name
	
	
	echo "        <Requirements>" >> $file_name
	
	# Tipo de recurso requerido.
	#    Valor para Procesador: 0
	#    Valor para Entrada/Salida: 1
        # En este caso siempre es procesador
	let "resource_type = 0"
	
	# Duracion del proceso.
	# La duracion del proceso no puede ser 0.
	let "life_time = ($RANDOM % $max_life_time) + 1"
	
	# Tiempo consumido por el proceso hasta el momento.
	consumed_time=0
	
	while [ "$consumed_time" -ne $life_time ]
	do

		let "resource_time = ($RANDOM % ($life_time - $consumed_time)) + 1"
		echo "            <UseTime>$resource_time</UseTime>" >> $file_name
		let "consumed_time += $resource_time"
		
	done
	
	echo "        </Requirements>" >> $file_name
	
	echo "    </Process>" >> $file_name
	
done


echo "</ProcessInfo>" >> $file_name

echo "    $file_name created succesfully!!!"

