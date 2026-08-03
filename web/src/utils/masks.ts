export function cepDigits(value: string): string {
  return value.replace(/\D/g, '').slice(0, 8)
}

/** 00000-000 */
export function formatCep(value: string): string {
  const digits = cepDigits(value)
  if (digits.length <= 5) return digits
  return `${digits.slice(0, 5)}-${digits.slice(5)}`
}

/** Sigla da UF em maiúsculas (ex.: SP). */
export function formatUf(value: string): string {
  return value.replace(/[^A-Za-z]/g, '').toUpperCase().slice(0, 2)
}

/** Número do endereço: dígitos, letras e separadores comuns (ex.: 123, 12A, S/N). */
export function formatAddressNumber(value: string): string {
  return value.replace(/[^0-9A-Za-z/\-]/g, '').slice(0, 12)
}
