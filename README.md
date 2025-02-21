# Tauri Plugin TinysInternalFS / Internal Storage Manager

# Important Notice

This plugin is primarily designed for Tinywang's convenience in developing Tauri applications and is tailored to personal needs only.

Currently, only the Android implementation is available. iOS support is pending.

This project is still under development and may be unstable.

## Introduction

This Tauri plugin implements internal storage functionality ( `/data/data/app.tauri/files` ) on Android without requiring permissions.

**Key Features:**

*   **`TFile` Class**:  Provides a file handle based approach for sequential file operations. It ensures operations are performed in order, especially for writing, by using a write queue.
*   **Immediate File Operations**: Offers functions for quick read and write operations (`readFileImmediately`, `writeFileImmediately`) without the need to manage file handles explicitly.
*   **File Existence Check**: Allows you to verify if a file exists at a given path (`checkFileExists`).
*   **Check if Path is File**: Allows you to verify if a path is a file (`checkIsFile`).
*   **Check if Path is Directory**: Allows you to verify if a path is a directory (`checkIsDir`).
*   **Close All Files**: Provides a function to close all files opened by the plugin (`closeFileAll`).
*   **Get Files Directory**: Retrieves the absolute path to the application's files directory on Android (`getFilesDir`).

**Implemented Features:**

- **Using `TFile` Class:**
    - Open file with mode (e.g., read, write)
    - Write content to file sequentially (`write`)
    - Read all content from file (`readAll`)
    - Close file (`close`)
- **Immediate File Operations:**
    - Read file immediately (`readFileImmediately`)
    - Write file immediately (`writeFileImmediately`)
- **Utilities:**
    - Check if file exists (`checkFileExists`)
    - Check if path is file (`checkIsFile`)
    - Check if path is directory (`checkIsDir`)
    - Close all files (`closeFileAll`)
    - Get Files Directory (`getFilesDir`)


## Library File Operation Function Implementation Status Table

|  Function Name (Module/Class)          | Android | iOS | Windows | macOS | Linux |
|------------------------------------------|---------|-----|---------|-------|-------|
| `TFile` Class                             | ✅      | ❌   | ❌      | ❌     | ❌     |
| `TFile.write()`                           | ✅      | ❌   | ❌      | ❌     | ❌     |
| `TFile.readAll()`                         | ✅      | ❌   | ❌      | ❌     | ❌     |
| `TFile.close()`                           | ✅      | ❌   | ❌      | ❌     | ❌     |
| `readFileImmediately()`                   | ✅      | ❌   | ❌      | ❌     | ❌     |
| `writeFileImmediately()`                  | ✅      | ❌   | ❌      | ❌     | ❌     |
| `checkFileExists()`                       | ✅      | ❌   | ❌      | ❌     | ❌     |
| `checkIsFile()`                           | ✅      | ❌   | ❌      | ❌     | ❌     |
| `checkIsDir()`                            | ✅      | ❌   | ❌      | ❌     | ❌     |
| `closeFileAll()`                          | ✅      | ❌   | ❌      | ❌     | ❌     |
| `getFilesDir()`                           | ✅      | ❌   | ❌      | ❌     | ❌     |

**Notes:**

*   ✅  : Implemented
*   ❌  : Not Implemented

## Usage

### Using `TFile` Class for File Operations

The `TFile` class is designed for scenarios where you need to perform multiple operations on a file in a controlled and sequential manner. It's particularly useful for ensuring write operations are executed in the order they are called.

**Opening a file and writing content:**

```typescript
import { TFile } from 'plugin-tinys-internal-fs-api';

async function writeFileExample() {
  try {
    const filePath = 'my_internal_file.txt';
    const file = new TFile(filePath, 'w'); // Open file in write mode
    await file.waitUntilInitialized(); // Ensure file is opened

    await file.write('First line of content.\n');
    await file.write('Second line of content.\n');
    await file.write('Third line of content.\n');

    await file.close(); // Close the file after writing
    console.log('File written successfully using TFile class.');

  } catch (error) {
    console.error('Failed to write file using TFile:', error);
  }
}

writeFileExample();
```

**Opening a file and reading all content:**

```typescript
import { TFile } from 'plugin-tinys-internal-fs-api';

async function readFileExample() {
  try {
    const filePath = 'my_internal_file.txt';
    const file = new TFile(filePath, 'r'); // Open file in read mode
    await file.waitUntilInitialized(); // Ensure file is opened

    const content = await file.readAll();
    console.log('File content:', content);

    await file.close(); // Close the file after reading (important to release resources)

  } catch (error) {
    console.error('Failed to read file using TFile:', error);
  }
}

readFileExample();
```

### Using Immediate Functions for Quick Operations

For simple, one-off read or write operations, you can use the immediate functions `readFileImmediately` and `writeFileImmediately`.

**Writing content immediately:**

```typescript
import { writeFileImmediately } from 'plugin-tinys-internal-fs-api';

async function writeImmediatelyExample() {
  try {
    const filePath = 'quick_write_file.txt';
    await writeFileImmediately(filePath, 'Content written immediately!');
    console.log('File written immediately.');
  } catch (error) {
    console.error('Failed to write file immediately:', error);
  }
}

writeImmediatelyExample();
```

**Reading content immediately:**

```typescript
import { readFileImmediately } from 'plugin-tinys-internal-fs-api';

async function readImmediatelyExample() {
  try {
    const filePath = 'quick_write_file.txt'; // Assuming this file exists
    const content = await readFileImmediately(filePath);
    console.log('File content read immediately:', content);
  } catch (error) {
    console.error('Failed to read file immediately:', error);
  }
}

readImmediatelyExample();
```

### Checking if a File Exists

```typescript
import { checkFileExists } from 'plugin-tinys-internal-fs-api';

async function checkFileExistenceExample() {
  try {
    const filePath = 'my_internal_file.txt';
    const exists = await checkFileExists(filePath);
    if (exists) {
      console.log(`File "${filePath}" exists.`);
    } else {
      console.log(`File "${filePath}" does not exist.`);
    }
  } catch (error) {
    console.error('Failed to check file existence:', error);
  }
}

checkFileExistenceExample();
```

### Checking if a Path is a File

```typescript
import { checkIsFile } from 'plugin-tinys-internal-fs-api';

async function checkIsFileExample() {
  try {
    const filePath = 'my_internal_file.txt'; // Replace with an actual file path
    const isFile = await checkIsFile(filePath);
    if (isFile) {
      console.log(`Path "${filePath}" is a file.`);
    } else {
      console.log(`Path "${filePath}" is not a file.`);
    }
  } catch (error) {
    console.error('Failed to check if path is a file:', error);
  }
}

checkIsFileExample();
```

### Checking if a Path is a Directory

```typescript
import { checkIsDir } from 'plugin-tinys-internal-fs-api';

async function checkIsDirExample() {
  try {
    const dirPath = 'my_internal_directory'; // Replace with an actual directory path
    const isDir = await checkIsDir(dirPath);
    if (isDir) {
      console.log(`Path "${dirPath}" is a directory.`);
    } else {
      console.log(`Path "${dirPath}" is not a directory.`);
    }
  } catch (error) {
    console.error('Failed to check if path is a directory:', error);
  }
}

checkIsDirExample();
```

### Getting the Files Directory

This function retrieves the absolute path to the application's internal files directory on Android. This is useful when you need to know the base directory for storing files.

```typescript
import { getFilesDir } from 'plugin-tinys-internal-fs-api';

async function getFilesDirExample() {
  try {
    const filesDir = await getFilesDir();
    console.log('Files directory:', filesDir);
  } catch (error) {
    console.error('Failed to get files directory:', error);
  }
}

getFilesDirExample();
```

### Closing All Opened Files

It's good practice to close all files when your application is about to terminate or when you are finished with file operations to release resources.

```typescript
import { closeFileAll } from 'plugin-tinys-internal-fs-api';

async function closeAllFilesExample() {
  try {
    await closeFileAll();
    console.log('All files closed.');
  } catch (error) {
    console.error('Failed to close all files:', error);
  }
}

closeAllFilesExample();
```