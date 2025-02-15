use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<TinysInternalFs<R>> {
  Ok(TinysInternalFs(app.clone()))
}

/// Access to the tinys-internal-fs APIs.
pub struct TinysInternalFs<R: Runtime>(AppHandle<R>);

impl<R: Runtime> TinysInternalFs<R> {
  pub fn ping(&self, payload: PingRequest) -> crate::Result<PingResponse> {
    Ok(PingResponse {
      value: payload.value,
    })
  }
}
