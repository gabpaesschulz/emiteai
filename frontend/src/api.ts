import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? "http://localhost:8080",
});

export interface Pessoa {
  id: number;
  nome: string;
  telefone: string;
  cpf: string;
  cep: string;
  numero: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
}

export const PessoasAPI = {
  list: () => api.get<Pessoa[]>("/pessoas").then((r) => r.data),
  create: (p: Omit<Pessoa, "id">) =>
    api.post<Pessoa>("/pessoas", p).then((r) => r.data),
  update: (id: number, p: Partial<Pessoa>) =>
    api.put<Pessoa>(`/pessoas/${id}`, p).then((r) => r.data),
};

export interface Relatorio {
  id: number;
  status: "PENDENTE" | "PRONTO";
  caminho: string;
}

export const RelatoriosAPI = {
  gerar: () => api.post<Relatorio>("/relatorios").then((r) => r.data),
  buscar: (id: number) =>
    api.get<Relatorio>(`/relatorios/${id}`).then((r) => r.data),
};
