use serde::de::DeserializeOwned;
use tauri::{
  plugin::{PluginApi, PluginHandle},
  AppHandle, Runtime,
};

use crate::models::*;
use crate::TinysInternalFsExt;
#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_tinys_internal_fs);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
  _app: &AppHandle<R>,
  api: PluginApi<R, C>,
) -> crate::Result<TinysInternalFs<R>> {
  #[cfg(target_os = "android")]
  let handle = api.register_android_plugin("com.plugin.tinysinternalfs", "ISMPlugin")?;
  #[cfg(target_os = "ios")]
  let handle = api.register_ios_plugin(init_plugin_tinys_internal_fs)?;
  Ok(TinysInternalFs(handle))
}

/// Access to the tinys-internal-fs APIs.
pub struct TinysInternalFs<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> TinysInternalFs<R> {
  pub fn read_file_immediately(&self, payload: ReadFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("readFileImmediately", payload)
        .map_err(Into::into)
}
pub fn write_file_immediately(&self, payload: WriteFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("writeFileImmediately", payload)
        .map_err(Into::into)
}
pub fn open_file(&self, payload: OpenFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("openFile", payload)
        .map_err(Into::into)
}
pub fn close_file(&self, payload: CloseFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("closeFile", payload)
        .map_err(Into::into)
}
pub fn write_file(&self, payload: WriteFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("writeFile", payload)
        .map_err(Into::into)
}
pub fn read_file_all(&self, payload: ReadFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("readFileAll", payload)
        .map_err(Into::into)
}
pub fn close_file_all(&self) -> crate::Result<()> {
    self.0
        .run_mobile_plugin("closeFileAll", ())
        .map_err(Into::into)
}
pub fn delete_file(&self, payload: DeleteFilePayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("deleteFile", payload)
        .map_err(Into::into)
}

pub fn check_file_exists(&self, payload: CheckFileExistsPayload) -> crate::Result<KResult> {
    self.0
        .run_mobile_plugin("checkFileExists", payload)
        .map_err(Into::into)
}
}

pub fn on_drop<R: Runtime>(app: AppHandle<R>) -> crate::Result<()> {
  app.tinys_internal_fs().close_file_all()
}