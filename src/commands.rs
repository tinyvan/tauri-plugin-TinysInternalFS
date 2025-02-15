use tauri::{AppHandle, command, Runtime};

use crate::models::*;
use crate::TinysInternalFsExt;

#[command]
pub(crate) async fn read_file_immediately<R: Runtime>(
    app: AppHandle<R>,
    payload: ReadFilePayload,
) -> Result<String, String> {
    let ret = app
        .tinys_internal_fs()
        .read_file_immediately(payload);
    match ret {
        Ok(value) => {
            if value.success {
                Ok(value.content)
            } else {
                Err(value.content)
            }
        }
        Err(e) => Err(e.into()),
    }
}
#[command]
pub(crate) async fn write_file_immediately<R: Runtime>(
    app: AppHandle<R>,
    payload: WriteFilePayload,
) -> Result<(), String> {
    let ret = app
        .tinys_internal_fs()
        .write_file_immediately(payload);
    match ret {
        Ok(value) => {
            if value.success {
                Ok(())
            } else {
                Err(value.content)
            }
        }
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn open_file<R: Runtime>(
    app: AppHandle<R>,
    path: String,
    mode: String,
) -> Result<File, String> {
    let ret = app
        .tinys_internal_fs()
        .open_file(OpenFilePayload {
            path: path.clone(),
            mode: mode.clone(),
        });
    println!("open_file:{}", path);
    match ret {
        Ok(value) => {
            if value.success {
                println!("open_file:{}", value.success);
                Ok(File {
                    path: value.content,
                    mode: mode,
                })
            } else {
                Err(value.content)
                //Err(Error::Io(std::io::Error::new(std::io::ErrorKind::Other,value.content)))
            }
        }
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn close_file<R: Runtime>(
    app: AppHandle<R>,
    file_handle: File,
) -> Result<(), String> {
    let ret = app
        .tinys_internal_fs()
        .close_file(CloseFilePayload {
            path: file_handle.path,
        });
    match ret {
        Ok(value) => {
            if value.success {
                Ok(())
            } else {
                Err(value.content)
            }
        }
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn write_file<R: Runtime>(
    app: AppHandle<R>,
    file_handle: File,
    content: String,
) -> Result<(), String> {
    let ret = app
        .tinys_internal_fs()
        .write_file(WriteFilePayload {
            path: file_handle.path,
            content: content,
        });
    match ret {
        Ok(value) => {
            if value.success {
                Ok(())
            } else {
                Err(value.content)
            }
        }
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn read_file_all<R: Runtime>(
    app: AppHandle<R>,
    file_handle: File,
) -> Result<String, String> {
    let ret = app
        .tinys_internal_fs()
        .read_file_all(ReadFilePayload {
            path: file_handle.path,
        });
    match ret {
        Ok(value) => {
            if value.success {
                Ok(value.content)
            } else {
                Err(value.content)
            }
        }
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn close_file_all<R: Runtime>(app: AppHandle<R>) -> Result<(), String> {
    let ret = app.tinys_internal_fs().close_file_all();
    match ret {
        Ok(_) => Ok(()),
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn delete_file<R: Runtime>(app: AppHandle<R>, path: String) -> Result<(), String> {
    let ret = app
        .tinys_internal_fs()
        .delete_file(DeleteFilePayload { path: path });
    match ret {
        Ok(value) => {
            if value.success {
                Ok(())
            } else {
                Err(value.content)
            }
        }
        Err(e) => Err(e.into()),
    }
}

#[command]
pub(crate) async fn check_file_exists<R: Runtime>(
    app: AppHandle<R>,
    path: String,
) -> Result<bool, String> {
    let ret = app
        .tinys_internal_fs()
        .check_file_exists(CheckFileExistsPayload { path: path });
    match ret {
        Ok(value) => Ok(value.success),
        Err(e) => Err(e.into()),
    }
}
