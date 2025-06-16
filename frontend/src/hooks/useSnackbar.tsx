import { Alert, Snackbar, SnackbarCloseReason } from "@mui/material";
import { SyntheticEvent, useState } from "react";

type Severidade = "success" | "info" | "warning" | "error";

export default function useSnackbar() {
  const [state, setState] = useState<{
    open: boolean;
    msg: string;
    sev: Severidade;
  }>({
    open: false,
    msg: "",
    sev: "info",
  });

  const close = (
    _event: Event | SyntheticEvent<any, Event> | null,
    reason?: SnackbarCloseReason
  ) => {
    if (reason === "clickaway") return;
    setState((s) => ({ ...s, open: false }));
  };

  const SnackbarAlert = (
    <Snackbar open={state.open} autoHideDuration={4000} onClose={close}>
      <Alert severity={state.sev} onClose={close} variant="filled">
        {state.msg}
      </Alert>
    </Snackbar>
  );

  const notify = (msg: string, sev: Severidade = "info") =>
    setState({ open: true, msg, sev });

  return [SnackbarAlert, notify] as const;
}
