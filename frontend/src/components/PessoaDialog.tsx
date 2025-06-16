import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Stack,
  CircularProgress,
  InputAdornment,
} from "@mui/material";
import { ChangeEvent, useEffect, useState } from "react";
import useSnackbar from "../hooks/useSnackbar";
import type { Pessoa } from "../api";

/* ---------- tipos ---------- */
interface Props {
  open: boolean;
  pessoa?: Pessoa | null;
  onClose: () => void;
  onSave: (data: Omit<Pessoa, "id"> | Partial<Pessoa>) => void;
}

/* campos obrigatórios p/ validação rápida no <TextField required /> */
const obrig = ["nome", "telefone", "cpf", "cep", "numero"] as const;
type Campo =
  | (typeof obrig)[number]
  | "complemento"
  | "bairro"
  | "cidade"
  | "estado";

/* ---------- componente ---------- */
export default function PessoaDialog({ open, pessoa, onClose, onSave }: Props) {
  /* estado local do formulário */
  const [form, setForm] = useState<Omit<Pessoa, "id">>({
    nome: "",
    telefone: "",
    cpf: "",
    cep: "",
    numero: "",
    complemento: "",
    bairro: "",
    cidade: "",
    estado: "",
  });

  const [loadingCep, setLoadingCep] = useState(false);
  const [Snack, notify] = useSnackbar();

  /* quando abrir ou mudar pessoa ⇒ (re)popular form */
  useEffect(() => {
    setForm(
      pessoa ?? {
        nome: "",
        telefone: "",
        cpf: "",
        cep: "",
        numero: "",
        complemento: "",
        bairro: "",
        cidade: "",
        estado: "",
      }
    );
  }, [pessoa, open]);

  /* -------------------------------- busca CEP (ViaCEP + fallback) */
  const buscaCep = async (cepLimpo: string) => {
    try {
      setLoadingCep(true);

      // 1ª tentativa: ViaCEP
      let data = await fetch(`https://viacep.com.br/ws/${cepLimpo}/json/`).then(
        (r) => r.json()
      );

      // se ViaCEP devolveu { erro: true } lança p/ cair no catch
      if (data.erro) throw new Error("ViaCEP não encontrou CEP");

      // se ViaCEP não trouxe bairro/cidade, tenta BrasilAPI
      if (!data.bairro) {
        const bapi = await fetch(
          `https://brasilapi.com.br/api/cep/v1/${cepLimpo}`
        ).then((r) => r.json());
        if (!bapi.errors) {
          data = {
            ...data,
            bairro: bapi.neighborhood,
            localidade: bapi.city,
          };
        }
      }

      setForm((f) => ({
        ...f,
        bairro: data.bairro ?? "",
        cidade: data.localidade ?? "",
        estado: data.uf ?? "",
      }));
    } catch (err: any) {
      notify(err.message || "Falha ao consultar CEP", "warning");
    } finally {
      setLoadingCep(false);
    }
  };

  /* -------------------------------- handler genérico de inputs */
  const handle = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));

    if (name === "cep") {
      const limpo = value.replace(/\D/g, "");
      if (limpo.length === 8) buscaCep(limpo);
    }
  };

  /* -------------------------------- helper p/ TextField */
  const renderInput = (
    name: Campo,
    label: string,
    extra?: Record<string, unknown>
  ) => (
    <TextField
      key={name}
      name={name}
      label={label}
      value={(form as any)[name] ?? ""}
      onChange={handle}
      required={(obrig as readonly string[]).includes(name)}
      fullWidth
      {...extra}
    />
  );

  /* -------------------------------- salvar */
  const salvar = () => onSave(form);

  /* ---------- render ---------- */
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{pessoa ? "Editar pessoa" : "Nova pessoa"}</DialogTitle>

      <DialogContent dividers>
        <Stack spacing={2} sx={{ mt: 1 }}>
          {renderInput("nome", "Nome")}
          {renderInput("telefone", "Telefone")}
          {renderInput("cpf", "CPF", { disabled: Boolean(pessoa) })}
          {renderInput("cep", "CEP", {
            placeholder: "89035330",
            InputProps: {
              endAdornment: loadingCep && (
                <InputAdornment position="end">
                  <CircularProgress size={18} />
                </InputAdornment>
              ),
            },
          })}
          {renderInput("numero", "Número")}
          {renderInput("complemento", "Complemento")}
          {renderInput("bairro", "Bairro")}
          {renderInput("cidade", "Cidade")}
          {renderInput("estado", "UF")}
        </Stack>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button variant="contained" onClick={salvar}>
          Salvar
        </Button>
      </DialogActions>

      {Snack}
    </Dialog>
  );
}
