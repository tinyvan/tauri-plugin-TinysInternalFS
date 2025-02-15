import { invoke } from '@tauri-apps/api/core'

export type _FileHandle = {
  path: string,
  mode:string,
}

export class TFile {
  private _fileHandle:_FileHandle={path:"",mode:""};
  private _isInitialized:boolean = false;
  private _initializationPromise: Promise<void>;
  private _writeQueue: Promise<null> = Promise.resolve(null); // Promise chain for sequential writes

  constructor(path: string, mode: string) {
    this._initializationPromise = new Promise<void>((resolve, reject) => {
      invoke<_FileHandle>("plugin:tinys-internal-fs|open_file", {
        path: path,
        mode: mode
      })
      .then((r) => {
        this._fileHandle = r;
        this._isInitialized = true;
        resolve();
      })
      .catch((e) => {
        reject(e);
      });
    });
  }

  /**
   * Waits until the file is initialized.
   * @returns {Promise<void>}
   */
  async waitUntilInitialized(): Promise<void> {
    return this._initializationPromise;
  }

  /**
   * Checks if the file is initialized.
   * @returns {boolean}
   */
  get isInitialized():boolean {
    return this._isInitialized;
  }

  /**
   * Gets the file path.
   * @returns {string}
   */
  get path():string {
    return this._fileHandle.path;
  }

  /**
   * Gets the file mode.
   * @returns {string}
   */
  get mode():string {
    return this._fileHandle.mode;
  }


  /**
   * Writes content to the file sequentially. Each write operation will wait for the previous one to complete.
   * This provides a synchronous-like behavior for write operations from the user's perspective, ensuring order.
   * @param {string} content The content to write to the file.
   * @returns {Promise<void>} A Promise that resolves when the write operation is completed.
   */
  write(content:string): Promise<null> {
    // Queue the write operation
    this._writeQueue = this._writeQueue.then(async () => {
      await this.waitUntilInitialized(); // Ensure file is initialized before writing
      if(!this._isInitialized) {
        throw new Error("File is not initialized");
      }
      return invoke<null>("plugin:tinys-internal-fs|write_file",{
        fileHandle:this._fileHandle,
        content:content
      });
    }).catch(e => {
this._writeQueue = Promise.resolve(null);
      throw e; 
    });

    return this._writeQueue;
  }


  /**
   * Closes the file. It waits for all pending write operations to complete before closing.
   * @returns {Promise<null>}
   */
  async close():Promise<null> {
    await this._writeQueue; // Wait for all writes to finish
    return await invoke<null>("plugin:tinys-internal-fs|close_file",{
      fileHandle:this._fileHandle
    });
  }

  /**
   * Reads all content from the file.
   * @returns {Promise<string>}
   */
  async readAll():Promise<string> {
    await this.waitUntilInitialized(); // Ensure file is initialized before reading
    if(!this._isInitialized) {
      throw new Error("File is not initialized");
    }
    return await invoke<string>("plugin:tinys-internal-fs|read_file_all",{
      fileHandle:this._fileHandle
    });
  }
}

/**
 * Reads a file immediately without needing to open a TFile instance.
 * @param {string} path The path to the file.
 * @returns {Promise<String>}
 */
export async function readFileImmediately(path: string): Promise<String> {
  return await invoke<String>("plugin:tinys-internal-fs|read_file_immediately",
    {
      payload: {
        path:path
      }
    });
}

/**
 * Writes content to a file immediately without needing to open a TFile instance.
 * @param {string} path The path to the file.
 * @param {string} content The content to write to the file.
 * @returns {Promise<null>}
 */
export async function writeFileImmediately(path:string,content:string): Promise<null> {
  return await invoke<null>("plugin:tinys-internal-fs|write_file_immediately",{
    payload:{
      path:path,
      content:content
    }
  });
}


/**
 * Closes all opened files by the plugin.
 * @returns {Promise<null>}
 */
export async function closeFileAll():Promise<null> {
  return await invoke<null>("plugin:tinys-internal-fs|close_file_all");
}

/**
 * Checks if a file exists at the given path.
 * @param {string} path The path to the file.
 * @returns {Promise<boolean>}
 */
export async function checkFileExists(path:string):Promise<boolean> {
  return await invoke<boolean>("plugin:tinys-internal-fs|check_file_exists",{
    path:path
  });
}


// export async function openFile(path:string,mode:string): Promise<_FileHandle> {
//   return await invoke("plugin:tinys-internal-fs|open_file",{
//     path:path,
//     mode:mode
//   })
// }

// export async function closeFile(file:_FileHandle): Promise<null> {
//   return await invoke("plugin:tinys-internal-fs|close_file",{
//     fileHandle:file
//   })
// }

// export async function writeFile(file:_FileHandle,content:string): Promise<null> {
//   return await invoke("plugin:tinys-internal-fs|write_file",{
//     fileHandle:file,
//     content:content
//   })
// }

// export async function readFileAll(file:_FileHandle): Promise<String> {
//   return await invoke("plugin:tinys-internal-fs|read_file_all",{
//     fileHandle:file
//   })
// }

