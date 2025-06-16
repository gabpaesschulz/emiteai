export async function ViaCep(cep: string) {
  const res = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
  const obj = await res.json();
  if (obj.erro) throw new Error("CEP inválido");
  return obj as {
    bairro: string;
    localidade: string;
    uf: string;
  };
}
