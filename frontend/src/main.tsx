import * as React from "react";
import { createRoot } from "react-dom/client";
import { CssBaseline, ThemeProvider, createTheme } from "@mui/material";
import PessoasPage from "./pages/PessoasPage";

const theme = createTheme();

createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <PessoasPage />
    </ThemeProvider>
  </React.StrictMode>
);
