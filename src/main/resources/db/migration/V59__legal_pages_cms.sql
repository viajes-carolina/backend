-- ==============================================================================
-- Viajes Carolina — Migration V59: CMS para páginas legales/institucionales
-- ==============================================================================
-- Hace editable desde el admin el contenido de 5 páginas públicas: Términos y
-- condiciones, Política de privacidad, Política de cookies, Compromiso contra
-- la ESNNA y Constancia MINCETUR (esta última nueva). Cada página es una tabla
-- singleton (id = 1), siguiendo el mismo patrón que about_page/blog_hero.
--
-- Columnas comunes a las 5 tablas: eyebrow, title, introduction,
-- document_control_label/text (badge de vigencia/última actualización),
-- sections_json (lista variable de {title, body}), closing_title/body/link_label
-- (nota institucional de cierre). Columnas específicas: ver cada tabla.
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. legal_terms — Términos y condiciones (6 secciones)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS legal_terms (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow VARCHAR(160) NOT NULL DEFAULT 'Información legal',
    title VARCHAR(255) NOT NULL DEFAULT 'Términos y condiciones',
    introduction TEXT NOT NULL DEFAULT 'Aquí explicamos de forma clara las condiciones generales aplicables a las cotizaciones, reservas y servicios gestionados por Viajes Carolina.',
    document_control_label VARCHAR(160) NOT NULL DEFAULT 'Control documental',
    document_control_text VARCHAR(255) NOT NULL DEFAULT 'Última actualización · Agosto de 2026',
    sections_json JSONB NOT NULL DEFAULT '[
        {"title":"Alcance de nuestros servicios","body":"Viajes Carolina brinda orientación, cotización y gestión de servicios turísticos según las condiciones informadas para cada viaje. Cada propuesta puede incluir vuelos, alojamiento, traslados, actividades u otros servicios expresamente indicados."},
        {"title":"Cotizaciones y disponibilidad","body":"Las cotizaciones son referenciales hasta que la reserva y el pago hayan sido confirmados. Tarifas, cupos, horarios y condiciones pueden variar por decisión de aerolíneas, hoteles u operadores."},
        {"title":"Reservas y pagos","body":"La reserva queda confirmada cuando el cliente acepta la propuesta, entrega la información requerida y realiza el pago acordado. Los comprobantes y condiciones específicas se comunican antes de confirmar."},
        {"title":"Cambios, cancelaciones y reembolsos","body":"Cada proveedor aplica sus propias penalidades y restricciones. Antes de pagar, el cliente recibe las condiciones relevantes del servicio contratado."},
        {"title":"Responsabilidades del viajero","body":"El viajero debe revisar la vigencia de documentos, requisitos migratorios, sanitarios y cualquier condición necesaria para realizar el viaje."},
        {"title":"Atención y contacto","body":"Si necesitas aclarar una condición antes de reservar, puedes escribirnos. Queremos que tomes una decisión informada y sin presión."}
    ]'::jsonb,
    closing_title VARCHAR(255) NOT NULL DEFAULT 'Antes de confirmar un viaje',
    closing_body TEXT NOT NULL DEFAULT 'Te mostraremos el precio, las inclusiones, las restricciones y las condiciones particulares de tu propuesta.',
    closing_link_label VARCHAR(160) NOT NULL DEFAULT 'Resolver una duda por WhatsApp',
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_legal_terms_singleton CHECK (id = 1)
);

INSERT INTO legal_terms (id) VALUES (1) ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------------------------
-- 2. legal_privacy — Política de privacidad (7 secciones)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS legal_privacy (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow VARCHAR(160) NOT NULL DEFAULT 'Legal',
    title VARCHAR(255) NOT NULL DEFAULT 'Política de privacidad',
    introduction TEXT NOT NULL DEFAULT 'En Viajes Carolina protegemos los datos personales que nos confías para asesorarte y gestionar tu viaje, en cumplimiento de la Ley N.º 29733, Ley de Protección de Datos Personales, y su reglamento.',
    document_control_label VARCHAR(160) NOT NULL DEFAULT 'Control documental',
    document_control_text VARCHAR(255) NOT NULL DEFAULT 'Última actualización · Agosto de 2026',
    sections_json JSONB NOT NULL DEFAULT '[
        {"title":"Responsable del tratamiento","body":"Viajes Carolina S.A.C. es la responsable del banco de datos personales generado a partir de tu relación con nosotros. Puedes contactarnos a través de los canales publicados en este sitio para cualquier consulta sobre el tratamiento de tus datos."},
        {"title":"Datos que recopilamos","body":"Recopilamos datos de identificación (nombre, documento de identidad o pasaporte, fecha de nacimiento), datos de contacto (correo, teléfono), preferencias de viaje y, cuando corresponde, datos de pago procesados directamente por pasarelas certificadas; no almacenamos números de tarjeta en nuestros sistemas."},
        {"title":"Finalidad del tratamiento","body":"Usamos tus datos para elaborar cotizaciones, gestionar reservas, emitir comprobantes de pago, brindarte soporte durante el viaje, cumplir obligaciones legales y tributarias, y enviarte comunicaciones comerciales cuando nos autorizas a hacerlo."},
        {"title":"Base legal y consentimiento","body":"El tratamiento se basa en tu consentimiento al entregarnos tus datos por nuestros canales (formularios del sitio, WhatsApp, correo), en la ejecución del contrato de intermediación turística y en el cumplimiento de obligaciones legales aplicables al sector turismo."},
        {"title":"Conservación y compartición con terceros","body":"Compartimos únicamente los datos estrictamente necesarios con aerolíneas, hoteles, operadores turísticos y proveedores de pago para ejecutar el servicio contratado. No vendemos ni cedemos tus datos personales a terceros con fines distintos. Conservamos la información mientras dure la relación comercial o exista una obligación legal de conservarla."},
        {"title":"Tus derechos ARCO","body":"Puedes ejercer en cualquier momento tus derechos de acceso, rectificación, cancelación y oposición (ARCO), así como revocar tu consentimiento, escribiéndonos al correo de contacto publicado en este sitio."},
        {"title":"Seguridad de la información","body":"Aplicamos medidas técnicas y organizativas razonables para proteger tus datos personales frente a pérdida, acceso no autorizado, alteración o uso indebido."}
    ]'::jsonb,
    closing_title VARCHAR(255) NOT NULL DEFAULT '¿Tienes dudas sobre tus datos?',
    closing_body TEXT NOT NULL DEFAULT 'Escríbenos y te ayudamos a ejercer tus derechos ARCO o resolver cualquier consulta sobre el uso de tu información.',
    closing_link_label VARCHAR(160) NOT NULL DEFAULT 'Escribir por WhatsApp',
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_legal_privacy_singleton CHECK (id = 1)
);

INSERT INTO legal_privacy (id) VALUES (1) ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------------------------
-- 3. legal_cookies — Política de cookies (6 secciones + panel de preferencias)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS legal_cookies (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow VARCHAR(160) NOT NULL DEFAULT 'Legal',
    title VARCHAR(255) NOT NULL DEFAULT 'Política de cookies',
    introduction TEXT NOT NULL DEFAULT 'Este sitio utiliza cookies propias y de terceros para mejorar tu experiencia de navegación, recordar tus preferencias y entender cómo se usa el sitio.',
    document_control_label VARCHAR(160) NOT NULL DEFAULT 'Control documental',
    document_control_text VARCHAR(255) NOT NULL DEFAULT 'Última actualización · Agosto de 2026',
    sections_json JSONB NOT NULL DEFAULT '[
        {"title":"¿Qué son las cookies?","body":"Las cookies son pequeños archivos de texto que un sitio web guarda en tu navegador para recordar información sobre tu visita, como tus preferencias o el tiempo que permaneces navegando."},
        {"title":"Tipos de cookies que utilizamos","body":"Usamos tres categorías de cookies: esenciales, analíticas y de preferencias. Puedes revisar y ajustar cada una desde el panel de preferencias de este sitio."},
        {"title":"Cookies esenciales","body":"Son necesarias para que el sitio funcione correctamente: permiten la navegación básica y el uso de funciones indispensables. No pueden desactivarse porque el sitio dejaría de funcionar con normalidad."},
        {"title":"Cookies analíticas","body":"Nos ayudan a entender de forma agregada cómo se usa el sitio (páginas más visitadas, tiempo de navegación) para mejorar la experiencia. No identifican a la persona y puedes desactivarlas cuando quieras."},
        {"title":"Cookies de preferencias","body":"Recuerdan elecciones de navegación, como el idioma o configuraciones previamente seleccionadas, para que no tengas que repetirlas en cada visita."},
        {"title":"Cómo administrar tus cookies","body":"Puedes aceptar, rechazar o personalizar las cookies no esenciales desde el panel de preferencias de este sitio en cualquier momento, o desde la configuración de tu navegador."}
    ]'::jsonb,
    closing_title VARCHAR(255) NOT NULL DEFAULT '¿Tienes dudas sobre las cookies?',
    closing_body TEXT NOT NULL DEFAULT 'Escríbenos si necesitas más información sobre cómo usamos las cookies en este sitio.',
    closing_link_label VARCHAR(160) NOT NULL DEFAULT 'Escribir por WhatsApp',
    cookie_categories_json JSONB NOT NULL DEFAULT '[
        {"key":"essential","name":"Esenciales","description":"Necesarias para que el sitio funcione correctamente. No pueden desactivarse.","required":true},
        {"key":"analytics","name":"Analítica","description":"Nos ayudan a entender cómo se usa el sitio para poder mejorarlo. Puedes desactivarlas.","required":false},
        {"key":"preferences","name":"Preferencias","description":"Recuerdan tus elecciones de navegación, como el idioma u otras configuraciones.","required":false}
    ]'::jsonb,
    accept_all_label VARCHAR(160) NOT NULL DEFAULT 'Aceptar todas',
    save_preferences_label VARCHAR(160) NOT NULL DEFAULT 'Guardar preferencias',
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_legal_cookies_singleton CHECK (id = 1)
);

INSERT INTO legal_cookies (id) VALUES (1) ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------------------------
-- 4. legal_esnna — Compromiso contra la ESNNA (6 secciones + declaración institucional)
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS legal_esnna (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow VARCHAR(160) NOT NULL DEFAULT 'Legal',
    title VARCHAR(255) NOT NULL DEFAULT 'Compromiso contra la ESNNA',
    introduction TEXT NOT NULL DEFAULT 'En Viajes Carolina rechazamos y condenamos toda forma de Explotación Sexual de Niños, Niñas y Adolescentes (ESNNA) vinculada al turismo y los viajes, dentro y fuera del Perú.',
    document_control_label VARCHAR(160) NOT NULL DEFAULT 'Control documental',
    document_control_text VARCHAR(255) NOT NULL DEFAULT 'Última actualización · Agosto de 2026',
    declaration_eyebrow VARCHAR(160) NOT NULL DEFAULT 'Declaración institucional',
    declaration_title VARCHAR(255) NOT NULL DEFAULT 'Tolerancia cero frente a la ESNNA',
    declaration_body TEXT NOT NULL DEFAULT 'Como agencia de viajes y turismo, asumimos este compromiso en línea con el Código de Conducta Nacional contra la Explotación Sexual de Niños, Niñas y Adolescentes promovido por el Ministerio de Comercio Exterior y Turismo (MINCETUR) y la normativa peruana vigente sobre la materia.',
    sections_json JSONB NOT NULL DEFAULT '[
        {"title":"Nuestro compromiso","body":"No promovemos, facilitamos ni toleramos servicios, contenidos o conductas que expongan a niños, niñas o adolescentes a explotación sexual, bajo ninguna circunstancia."},
        {"title":"Capacitación de nuestro equipo","body":"Informamos y sensibilizamos a nuestro equipo sobre esta problemática y las señales de alerta asociadas al turismo, para que sepan identificar y actuar ante situaciones de riesgo."},
        {"title":"Colaboración con las autoridades","body":"Colaboramos activamente con la Policía Nacional del Perú, el Ministerio Público y demás autoridades competentes ante cualquier indicio o denuncia relacionada con estos hechos."},
        {"title":"Turismo responsable","body":"Orientamos a nuestros clientes hacia un turismo responsable, seguro y respetuoso de los derechos de la niñez y la adolescencia en cada destino que visitan."},
        {"title":"Selección de proveedores","body":"Priorizamos el trabajo con operadores, hoteles y prestadores de servicios turísticos que comparten y aplican políticas de protección de niños, niñas y adolescentes."},
        {"title":"Cómo reportar","body":"Si tienes conocimiento de una situación de este tipo, repórtala de inmediato a la Policía Nacional del Perú o al Ministerio Público, o escríbenos directamente para orientarte sobre cómo proceder."}
    ]'::jsonb,
    closing_title VARCHAR(255) NOT NULL DEFAULT 'Un turismo seguro para todos',
    closing_body TEXT NOT NULL DEFAULT 'Si necesitas reportar una situación o tienes dudas sobre este compromiso, contáctanos directamente.',
    closing_link_label VARCHAR(160) NOT NULL DEFAULT 'Contactar por WhatsApp',
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_legal_esnna_singleton CHECK (id = 1)
);

INSERT INTO legal_esnna (id) VALUES (1) ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------------------------
-- 5. legal_mincetur — Constancia MINCETUR (4 secciones + verificación) — página nueva
-- ------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS legal_mincetur (
    id BIGINT PRIMARY KEY DEFAULT 1,
    eyebrow VARCHAR(160) NOT NULL DEFAULT 'Legal',
    title VARCHAR(255) NOT NULL DEFAULT 'Constancia MINCETUR',
    introduction TEXT NOT NULL DEFAULT 'Viajes Carolina se encuentra registrada ante el Ministerio de Comercio Exterior y Turismo (MINCETUR) como agencia de viajes y turismo, en cumplimiento de la normativa vigente del sector.',
    document_control_label VARCHAR(160) NOT NULL DEFAULT 'Control documental',
    document_control_text VARCHAR(255) NOT NULL DEFAULT 'Última actualización · Agosto de 2026',
    sections_json JSONB NOT NULL DEFAULT '[
        {"title":"¿Qué es el registro MINCETUR?","body":"El Registro de Agencias de Viajes y Turismo del MINCETUR certifica que una empresa cumple los requisitos legales y técnicos exigidos para operar formalmente en el sector turismo en el Perú."},
        {"title":"Nuestro registro","body":"Viajes Carolina cuenta con constancia vigente de inscripción ante MINCETUR, verificable de forma pública en el portal oficial de la entidad."},
        {"title":"Qué garantiza para ti","body":"Trabajar con una agencia formalmente registrada te da respaldo legal, trazabilidad de tus pagos y acceso a los mecanismos oficiales de reclamo, como el Libro de Reclamaciones e INDECOPI."},
        {"title":"Vigencia y actualización","body":"Este registro se mantiene vigente mediante actualizaciones periódicas ante MINCETUR. Cualquier cambio en nuestra razón social o representación legal se refleja también en el portal oficial de la entidad."}
    ]'::jsonb,
    closing_title VARCHAR(255) NOT NULL DEFAULT '¿Tienes dudas sobre nuestro registro?',
    closing_body TEXT NOT NULL DEFAULT 'Escríbenos si necesitas más información sobre nuestra formalidad como agencia de viajes.',
    closing_link_label VARCHAR(160) NOT NULL DEFAULT 'Escribir por WhatsApp',
    verification_eyebrow VARCHAR(160) NOT NULL DEFAULT 'Verificación MINCETUR',
    verification_button_label VARCHAR(160) NOT NULL DEFAULT 'Ver constancia en MINCETUR ↗',
    verification_note TEXT NOT NULL DEFAULT 'Puedes verificar esta información directamente en el portal oficial de MINCETUR ingresando el número de registro indicado en esta página.',
    revision INT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_legal_mincetur_singleton CHECK (id = 1)
);

INSERT INTO legal_mincetur (id) VALUES (1) ON CONFLICT (id) DO NOTHING;
