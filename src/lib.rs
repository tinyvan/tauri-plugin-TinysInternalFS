#![cfg(mobile)]
use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::TinysInternalFs;
#[cfg(mobile)]
use mobile::TinysInternalFs;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the tinys-internal-fs APIs.
pub trait TinysInternalFsExt<R: Runtime> {
  fn tinys_internal_fs(&self) -> &TinysInternalFs<R>;
}

impl<R: Runtime, T: Manager<R>> crate::TinysInternalFsExt<R> for T {
  fn tinys_internal_fs(&self) -> &TinysInternalFs<R> {
    self.state::<TinysInternalFs<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("tinys-internal-fs")
    .invoke_handler(tauri::generate_handler![
      commands::read_file_immediately,
      commands::write_file_immediately,
      commands::open_file,
      commands::close_file,
      commands::write_file,
      commands::read_file_all,
      commands::close_file_all,
      commands::delete_file,
      commands::check_file_exists,
      commands::get_files_dir,
      commands::check_is_file,
      commands::check_is_dir,
    ])
    .setup(|app, api| {
      #[cfg(mobile)]
      let tinys_internal_fs = mobile::init(app, api)?;
      #[cfg(desktop)]
      let tinys_internal_fs = desktop::init(app, api)?;
      app.manage(tinys_internal_fs);
      Ok(())
    })
    .on_drop(|app| {
      #[cfg(mobile)]
      mobile::on_drop(app).unwrap();
  })
  .build()
}
