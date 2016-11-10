import json
import math
import os
import configparser
import sys

#Import statistics module
try :
	import statistics
except ImportError:
	if( os.name is "posix") :
		print("Please install statistics by using the command: 'sudo pip install statistics'")
	else :
		print("Please install statistics by using the command: 'pip install statistics'")
	sys.exit()

#Import plotly module
try :
	import plotly.plotly as py
	import plotly.graph_objs as go
except ImportError:
	if( os.name is "posix") :
		print("Please install plotly by using the command: 'sudo pip install plotly'")
	else :
		print("Please install plotly by using the command: 'pip install plotly'")
	sys.exit()
	
# Import colorama module
try:
	import colorama
	colorama.init()
	from colorama import init, Fore, Back, Style

except ImportError: 
	if( os.name is "posix") :
		print("Please install colorama by using the command: 'sudo pip install colorama'")
	else :
		print("Please install colorama by using the command: 'pip install colorama'")
	sys.exit()

# Attributes stored across a single file run
attributes = set()
vehicle_speed = []
engine_speed = []
accelerator_pedal_position = []

# Attributes stored across all file runs
vehicle_speed_all = []
engine_speed_all = []
accelerator_pedal_position_all = []

# Configuration settings (initialized)
AllowZeroValues = False 
VehicleSpeedGrouping = 0
EngineSpeedGrouping = 0
AcceleratorPedalPositionGrouping = 0

def main() :
	
	executableName = sys.argv[0];
	ignoredFiles = set()
	
	# Command line args
	for arg in sys.argv :
		
		# Help command
		if( arg.lower() == "--help" or arg.lower() == '/help' or arg.lower() == '-help') :
			print( "Usage: python " + executableName + " [ignored files]")
			sys.exit(1);
		
		# Ignored files
		if( arg.lower().endswith(".json") ) :
			ignoredFiles.add(arg.lower());
	
	# Print the ignored files
	if( len(ignoredFiles) == 0 ) :
		print("Going through all json files in the current directory")
	else :
		print("Ignoring files: " + str(ignoredFiles))
	
	# Reads the configuration file
	readConfig()

	# Get the files&directories in the current directory
	files = os.listdir(os.curdir)
	
	# Iterate through all files
	for file in files:
	
		# Only look for json files
		if(file.endswith(".json") and file.lower() not in ignoredFiles) :
			openJson(file);

	# Final run for combined statistics
	doStatistics(True)
		
# Sets up and reads the configuration
def readConfig() :
	# Configuration file setup
	config = configparser.ConfigParser()
	config.read('config.ini')
	
	# Make sure we are updating the global variables
	global AllowZeroValues
	global VehicleSpeedGrouping
	global EngineSpeedGrouping
	global AcceleratorPedalPositionGrouping
	
	# Read the configuration settings and set global variables
	AllowZeroValues                  = config['boolean']['AllowZeroValues'] == 'True'
	VehicleSpeedGrouping             = int(config['integer']['VehicleSpeedGrouping'])
	EngineSpeedGrouping              = int(config['integer']['EngineSpeedGrouping'])
	AcceleratorPedalPositionGrouping = int(config['integer']['AcceleratorPedalPositionGrouping'])
	
	# Print out the configuration for the user to see
	print(Fore.GREEN + "Configuration: \n\tAllowZeroValues: " + str(AllowZeroValues) \
		+ "\n\tVehicleSpeedGrouping: " + str(VehicleSpeedGrouping) \
		+ "\n\tEngineSpeedGrouping: " + str(EngineSpeedGrouping) \
		+ "\n\tAcceleratorPedalPositionGrouping: " + str(AcceleratorPedalPositionGrouping) + Fore.RESET)
	
	
# Opens and parses json in each file
def openJson(fileName) :
	print ("Going through file: " + fileName)
	
	# Open the file and read line by line
	file = open(fileName, "r")
	for line in file:
		parseJsonLine(line)
	
	# Perform statistics on someone
	doStatistics(False)
	
	# Delete the contents from the lists
	del vehicle_speed[:]
	del engine_speed[:]
	del accelerator_pedal_position[:]
		
# Parse a single json portion (e.g. a line)
def parseJsonLine(line) :
	
	# Grab the global variables to make sure we are using them instead of local variables
	global vehicle_speed
	global engine_speed
	global engine_speed
	global vehicle_speed_all
	global engine_speed_all
	global accelerator_pedal_position_all	
	
	try :
		# Load the line as JSon
		data = json.loads(line)
		
		# Load the data in based on the name of the attribute
		if( 'name' in data and 'value' in data ) :
			attributes.add(data['name'])
			
			# Add the corresponding value to the array
			if(data['name'].lower() == "vehicle_speed") :
				if( float(data['value']) != 0 and not AllowZeroValues ) :
					vehicle_speed.append(float(data['value']))
					vehicle_speed_all.append(float(data['value']))
				elif( AllowZeroValues ) :
					vehicle_speed.append(float(data['value']))
					vehicle_speed_all.append(float(data['value']))
			elif(data['name'].lower() == "engine_speed") :
				if( float(data['value']) != 0 ) :
					engine_speed.append(float(data['value']))
					engine_speed_all.append(float(data['value']))
				elif( AllowZeroValues ) :
					engine_speed.append(float(data['value']))
					engine_speed_all.append(float(data['value']))
			elif(data['name'].lower() == "accelerator_pedal_position") :
				if( float(data['value']) != 0 and not AllowZeroValues ) :
					accelerator_pedal_position.append(float(data['value']))
					accelerator_pedal_position_all.append(float(data['value']))
				elif( AllowZeroValues ) :
					accelerator_pedal_position.append(float(data['value']))
					accelerator_pedal_position_all.append(float(data['value']))
					
	except Exception: 
		print(Fore.RED + "[Error] Problem reading line: " + line + " " + Fore.RESET)

# Prints out the corresponding statistics about each of the categories for the file[s]
def doStatistics(final) :

	try :
		if(not final) :
			print(Fore.RED + "\tLen vehicle_speed " + str(len(vehicle_speed)) + Fore.RESET);
			print(Fore.RED + "\tLen engine speed " + str(len(engine_speed)) + Fore.RESET);
			print(Fore.RED + "\tLen accelerator " + str(len(accelerator_pedal_position)) + Fore.RESET);
			print(Fore.RED + "\tLen vehicle_speed_all " + str(len(vehicle_speed_all)) + Fore.RESET);
			print(Fore.GREEN + "\tVehicle speed average: " + str(statistics.mean(vehicle_speed)) + Fore.RESET)
			print(Fore.GREEN + "\tMedian vehicle speed: " + str(statistics.median(vehicle_speed)) + Fore.RESET)
			print(Fore.GREEN + "\tHighest vehicle speed: " + str(max(vehicle_speed)) + Fore.RESET)
			print(Fore.GREEN + "\tLowest vehicle speed: " + str(min(vehicle_speed)) + Fore.RESET)
			print()
			
			print(Fore.GREEN + "\tEngine speed average: " + str(statistics.mean(engine_speed)) + Fore.RESET)
			print(Fore.GREEN + "\tMedian engine speed: " + str(statistics.median(engine_speed)) + Fore.RESET)
			print(Fore.GREEN + "\tEngine Speed max: " + str(max(engine_speed)) + Fore.RESET)
			print(Fore.GREEN + "\tEngine speed min: " + str(min(engine_speed)) + Fore.RESET)
			print()
			
			print(Fore.GREEN + "\tAccelerator pedal position average: " + str(statistics.mean(accelerator_pedal_position)) + Fore.RESET)
			print(Fore.GREEN + "\tMedian accelerator pedal position average: " + str(statistics.median(accelerator_pedal_position)) + Fore.RESET)
			print(Fore.GREEN + "\tAccelerator pedal position max: " + str(max(accelerator_pedal_position)) + Fore.RESET)
			print(Fore.GREEN + "\tAccelerator pedal position min: " + str(min(accelerator_pedal_position)) + Fore.RESET)
		
		else :
			print(Fore.YELLOW + "\nCombined Statistics: " + Fore.RESET)
			
			print(Fore.GREEN + "\tVehicle speed average: " + str(statistics.mean(vehicle_speed_all)) + Fore.RESET)
			print(Fore.GREEN + "\tMedian vehicle speed: " + str(statistics.median(vehicle_speed_all)) + Fore.RESET)
			print()
			
			print(Fore.GREEN + "\tEngine speed average: " + str(statistics.mean(engine_speed_all)) + Fore.RESET)
			print(Fore.GREEN + "\tMedian engine speed: " + str(statistics.median(engine_speed_all)) + Fore.RESET)
			print()
			
			print(Fore.GREEN + "\tAccelerator pedal position average: " + str(statistics.mean(accelerator_pedal_position_all)) + Fore.RESET)
			print(Fore.GREEN + "\tMedian accelerator pedal position average: " + str(statistics.median(accelerator_pedal_position_all)) + Fore.RESET)
		
        # Do graph calculations
		graphPoints(final)

	except statistics.StatisticsError :
		print(Fore.RED + "\t[Error] Not enough datapoints (probably)" + Fore.RESET)

# Groups the points and then graphs them
def graphPoints(final) :
    # TODO <BMV> Add in code to group for barchart
	
	# Graph the _all lists
	if( final ) :
		layout = go.Layout(
			title="All vehicle speed"
		)
		
		
		layout = go.Layout(
			title="All engine speed"
		)
		
		
		
		layout = go.Layout(
			title ="All accel. pedal pos."
		)
		
	# Graph normally
	else :
		print("Every other graph")
		
    
# Counts and partitions the points of an array based on the position
def countPoints(idx_num) :
	
	# Vehicle speed
	if( idx_num == 0 ) :
	
	# Engine speed
	elif( idx_num == 1 ) :
	
	# Accl pedal pos 
	elif( idx_num == 2 ) :
	
	return list	
if __name__ == "__main__":
	main()