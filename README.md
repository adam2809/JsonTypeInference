# Json Type Inference


### Project description
This project provides you with a way to automatically create data models of JSON files and functions to read into them.

### What is and is not supported
Arbitrary nesting of objects is supported but only arrays that contain boolean, string or number values will work properly. If they don't a List\<Any> type will be assumed. As many APIs send back an array of objects the usage I recommend is to only give the program the first element of the array and when using a deserialization library tell it that the JSON file has structure List<\[Generated data class name]>. Another quirk is that if the JSON file provided is not an object then the data class generated will have a topLevel property with an appropriate type.

### Usage
To use this project add an entry to the JSON array in src/main/resources/config.json with this structure:
``` json
{
    "source": "Path to a file with a sample JSON based on which data classes will be created",
    "dest": "Path to a file where the generated data classes will be placed",
    "className": "Name of the top level data class"
}
```     
Run the program to update all files provided in config.json