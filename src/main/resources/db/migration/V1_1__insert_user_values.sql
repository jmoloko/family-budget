INSERT INTO users
VALUES
    (DEFAULT, 'johndoe@yahoo.com', 'John Doe', '$2a$12$2b75/kZRtObHp4M88ZFEQe.FMM8P9r.aK6yl/OSI0AzA9xo.ucBca', 'ADMIN', 'ACTIVE'), -- pass-admin
    (DEFAULT, 'mike@mail.ru', 'Mike Snow', '$2a$12$2b75/kZRtObHp4M88ZFEQe.FMM8P9r.aK6yl/OSI0AzA9xo.ucBca', 'MODERATOR', 'ACTIVE'), -- pass-admin
    (DEFAULT, 'user@gmail.com', 'Test User', '$2a$12$qmyqcf.oco6EFN/C74Dxne.Xo8fcKPEKyT0WUV3sYXTO8aiYwSlyS', 'USER', 'ACTIVE'); -- pass-user