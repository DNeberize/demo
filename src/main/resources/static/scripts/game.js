const state = window.WORDE_STATE;

if (state) {
  const board = document.querySelector('[data-board]');
  const form = document.querySelector('[data-guess-form]');
  const input = document.querySelector('#guessInput');
  const notice = document.querySelector('[data-notice]');
  const attemptsLeft = document.querySelector('[data-attempts-left]');
  const submitButton = document.querySelector('[data-submit-button]');
  const wordPattern = /^\p{L}{5}$/u;

  const renderBoard = () => {
    if (!board) {
      return;
    }

    board.innerHTML = '';

    for (let rowIndex = 0; rowIndex < state.maxAttempts; rowIndex += 1) {
      const row = state.guesses[rowIndex] ?? { guess: '', result: [] };
      const rowElement = document.createElement('div');
      rowElement.className = 'board-row';

      const letters = Array.from(row.guess ?? '');

      for (let columnIndex = 0; columnIndex < 5; columnIndex += 1) {
        const tile = document.createElement('div');
        tile.className = 'tile';

        const letter = letters[columnIndex] ?? '';
        const code = row.result[columnIndex];

        if (letter) {
          tile.textContent = letter;
          if (code === 1) {
            tile.classList.add('is-hit');
          }
          else if (code === 2) {
            tile.classList.add('is-near');
          }
          else {
            tile.classList.add('is-miss');
          }
        }
        else {
          tile.classList.add('is-empty');
        }

        rowElement.appendChild(tile);
      }

      board.appendChild(rowElement);
    }
  };

  const showNotice = (message, isError = false) => {
    if (!notice) {
      return;
    }
    notice.innerHTML = message;
    notice.classList.add('is-open');
    notice.style.color = isError ? '#8f2d2d' : '';
  };

  const syncAttempts = () => {
    if (attemptsLeft) {
      attemptsLeft.textContent = `${state.attemptsRemaining} attempts left`;
    }
  };

  renderBoard();
  syncAttempts();

  if (form && input && submitButton) {
    if (state.finished) {
      input.disabled = true;
      submitButton.disabled = true;
    }

    form.addEventListener('submit', async event => {
      event.preventDefault();

      if (state.finished) {
        return;
      }

      const guess = input.value.trim().toLowerCase();

      if (!wordPattern.test(guess)) {
        showNotice('Enter exactly 5 letters. Numbers and symbols are not allowed.', true);
        return;
      }

      submitButton.disabled = true;

      try {
        const response = await fetch('/api/game/guess', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json'
          },
          body: JSON.stringify({ guess })
        });

        const payload = await response.json();

        if (!response.ok) {
          showNotice(payload.message ?? 'Guess rejected.', true);
          return;
        }

        state.guesses.push({ guess: payload.guess, result: payload.result });
        state.attemptsRemaining = payload.attemptsRemaining;
        state.finished = payload.finished;
        state.won = payload.solved;

        renderBoard();
        syncAttempts();
        input.value = '';

        if (payload.solved) {
          showNotice('Perfect guess. Loading the round summary...');
        }
        else if (payload.finished) {
          showNotice('Round finished. Loading the round summary...');
        }
        else {
          showNotice('Guess saved. Keep going.');
        }

        if (payload.finished) {
          input.disabled = true;
          submitButton.disabled = true;
          window.setTimeout(() => window.location.reload(), 900);
          return;
        }
      }
      catch (error) {
        showNotice('Network error. Try again.', true);
      }
      finally {
        if (!state.finished) {
          submitButton.disabled = false;
        }
      }
    });
  }
}