const COMMANDS: &[&str] = &[
    "read_file_immediately",
    "write_file_immediately",
    "open_file",
    "close_file",
    "write_file",
    "read_file_all",
    "close_file_all",
    "delete_file",
    "check_file_exists",
];

fn main() {
  tauri_plugin::Builder::new(COMMANDS)
    .android_path("android")
    .ios_path("ios")
    .build();
}
