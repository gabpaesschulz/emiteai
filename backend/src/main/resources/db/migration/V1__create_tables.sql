CREATE TABLE pessoa (
    id           BIGSERIAL PRIMARY KEY,

    nome         VARCHAR(150) NOT NULL,
    telefone     VARCHAR(20)  NOT NULL,

    cpf          VARCHAR(11) UNIQUE NOT NULL
                 CHECK (cpf ~ '^[0-9]{11}$'),

    cep          VARCHAR(8)  NOT NULL
                 CHECK (cep ~ '^[0-9]{8}$'),

    numero       VARCHAR(10) NOT NULL,
    complemento  VARCHAR(60),

    bairro       VARCHAR(100),
    cidade       VARCHAR(100),
    estado       VARCHAR(2),

    criado_em    TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE audit_log (
    id           BIGSERIAL PRIMARY KEY,
    rota         VARCHAR(120) NOT NULL,
    metodo       VARCHAR(8)   NOT NULL,
    corpo        TEXT,
    ip           INET,
    user_agent   TEXT,
    criado_em    TIMESTAMPTZ  DEFAULT now()
);

CREATE TABLE relatorio (
    id           BIGSERIAL PRIMARY KEY,
    caminho      TEXT        NOT NULL,
    status       VARCHAR(20) NOT NULL,
    criado_em    TIMESTAMPTZ DEFAULT now(),
    pronto_em    TIMESTAMPTZ
);
