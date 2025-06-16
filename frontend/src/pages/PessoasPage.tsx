import { Box, Button, Container, Typography } from "@mui/material";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { useEffect, useState } from "react";
import { PessoasAPI, RelatoriosAPI } from "../api";
import PessoaDialog from "../components/PessoaDialog";
import useSnackbar from "../hooks/useSnackbar";
import type { Pessoa } from "../api";

const API_URL = import.meta.env.VITE_API ?? "http://localhost:8080";

export default function PessoasPage() {
  const [rows, setRows] = useState<Pessoa[]>([]);
  const [open, setOpen] = useState(false);
  const [edit, setEdit] = useState<Pessoa | null>(null);
  const [Snack, notify] = useSnackbar();

  const carregar = async () => {
    const lista = await PessoasAPI.list();
    setRows(lista);
  };
  useEffect(() => {
    carregar();
  }, []);

  const colunas: GridColDef[] = [
    { field: "id", headerName: "ID", width: 80 },
    { field: "nome", flex: 1 },
    { field: "cpf", width: 140 },
    { field: "telefone", width: 140 },
    { field: "cep", width: 110 },
    { field: "numero", headerName: "Nº", width: 90 },
    { field: "bairro", width: 160 },
    { field: "cidade", width: 160 },
    { field: "estado", width: 70 },
  ];

  const salvar = async (data: Omit<Pessoa, "id"> | Partial<Pessoa>) => {
    try {
      if (edit) {
        await PessoasAPI.update(edit.id!, data);
      } else {
        await PessoasAPI.create(data as Omit<Pessoa, "id">);
      }
      notify("Salvo!", "success");
      setOpen(false);
      setEdit(null);
      carregar();
    } catch (e: any) {
      notify(e?.response?.data?.message || e?.message || "Erro", "error");
    }
  };

  const gerarRelatorio = async () => {
    try {
      const { id } = await RelatoriosAPI.gerar();
      notify("Gerando CSV… aguarde");

      const maxTentativas = 10;
      const delay = 3_000;

      for (let tentativa = 1; tentativa <= maxTentativas; tentativa++) {
        const rep = await RelatoriosAPI.buscar(id);

        if (rep.status === "PRONTO") {
          const resp = await fetch(`${API_URL}/relatorios/${id}/download`, {});

          if (!resp.ok) throw new Error("Falha no download");

          const blob = await resp.blob();
          const url = URL.createObjectURL(blob);

          const a = Object.assign(document.createElement("a"), {
            href: url,
            download: `relatorio-${id}.csv`,
          });
          document.body.appendChild(a);
          a.click();
          a.remove();
          URL.revokeObjectURL(url);

          notify("Download iniciado!", "success");
          return;
        }

        await new Promise((r) => setTimeout(r, delay));
      }

      notify("Ainda processando… tente novamente em instantes.", "warning");
    } catch (err: any) {
      notify(err?.message || "Erro ao gerar relatório", "error");
    }
  };

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Pessoas
      </Typography>

      <Box sx={{ height: 420, mb: 2 }}>
        <DataGrid
          rows={rows}
          columns={colunas}
          getRowId={(r) => r.id}
          pageSizeOptions={[5, 10, 25]}
          autoHeight
          disableRowSelectionOnClick
          onRowDoubleClick={({ row }) => {
            setEdit(row as Pessoa);
            setOpen(true);
          }}
        />
      </Box>

      <Button
        variant="contained"
        sx={{ mr: 2 }}
        onClick={() => {
          setEdit(null);
          setOpen(true);
        }}
      >
        Nova pessoa
      </Button>

      <Button variant="outlined" onClick={gerarRelatorio}>
        Gerar CSV
      </Button>

      <PessoaDialog
        open={open}
        pessoa={edit}
        onClose={() => setOpen(false)}
        onSave={salvar}
      />

      {Snack}
    </Container>
  );
}
